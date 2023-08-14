package com.qx.ctrl;

import com.alibaba.fastjson.JSON;
import com.qx.SseRespInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiux
 * @date 2022/11/25
 * @since
 */
@RestController
@Slf4j
@RequestMapping("/sse/message")
public class SSeMessageNoticeController {

    private Map<String, PrintWriter> responseMap = new ConcurrentHashMap<>();

    private List<String> exculdeIp = Arrays.asList("127.0.0.1", "localhost", "0.0.0.0");

    @GetMapping(path = "/subscribe")
    public WebAsyncTask connect(HttpServletResponse response, @RequestParam String id) {
        log.info("ss subscribe , token:{}", id);
        String sseConnectId = id;

        PrintWriter oldPrintWriter = responseMap.get(id);
        if (oldPrintWriter != null) {
            responseMap.remove(id);
            oldPrintWriter.close();
        }

        WebAsyncTask<Void> webAsyncTask = new WebAsyncTask<>(24 * 60 * 60 * 1000L, () -> {
            log.info("sse subscribe token {}, callable", sseConnectId);
            PrintWriter writer = null;
            try {
                response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                response.setHeader("X-Accel-Buffering", "no");
                response.setHeader("Cache-Control", "no-cache");
                writer = response.getWriter();
                responseMap.put(sseConnectId, writer);

            } catch (IOException e) {
                log.error("sse 建立连接失败： {}", sseConnectId, e);
            }
            log.info("sse subscribe token {}, print write", sseConnectId);

            writeSseInfo(sseConnectId, "订阅成功");
//            writeSseInfo(sseConnectId, sseRespInfoBO, false);
            log.info("sse subscribe token {}, print write end", sseConnectId);
            while (true) {
                Thread.sleep(100);
                if (!responseMap.containsKey(sseConnectId)) {
                    break;
                }
            }
            return null;
        });
        webAsyncTask.onCompletion(() -> log.info("程序正常执行完成的回调"));

        webAsyncTask.onTimeout(() -> {
            PrintWriter printWriter = responseMap.get(sseConnectId);
            if (printWriter != null) {
                printWriter.close();
            }
            responseMap.remove(sseConnectId);
            log.info("超时了");
            return null;
        });
        webAsyncTask.onError(() -> {
            log.error("出现异常");
            return null;
        });
        return webAsyncTask;

    }

    @GetMapping(path = "/test")
    public void sentCount(String id, String msg) {
        SseRespInfoBO sseRespInfoBO = SseRespInfoBO.builder().id(UUID.randomUUID().toString().replace("-", "")).data(msg).event("test").build();
        writeSseInfo(id, sseRespInfoBO);

    }

//    @GetMapping(path = "/send-sse-msg")
//    public Result sendSseMsg(@Valid SseMsgInfoDTO sseSendMsgDTO) {
//
//        PrintWriter printWriter = responseMap.get(sseSendMsgDTO.getSseConnectId());
//        if (printWriter == null) {
//            log.error("{}连接已经失效", sseSendMsgDTO.getSseConnectId());
//            return Result.failed("连接已经失效");
//        }
//        SseRespInfoBO sseRespInfoBO = SseRespInfoBO.builder()
//                .data(sseSendMsgDTO.getMsg())
//                .event(sseSendMsgDTO.getEvent()).
//                id(UUID.randomUUID().toString().replace("-", ""))
//                .build();
//        writeSseInfo(sseSendMsgDTO.getSseConnectId(), sseRespInfoBO);
//
//        return Result.ok();
//    }

//    @PostMapping(path = "/send-sse-msg-by-connect")
//    public Result sendSseMsgListBySeeConnectId(@Valid @RequestBody SseMsgSendDTO sseMsgSendDTO) {
//
//        PrintWriter printWriter = responseMap.get(sseMsgSendDTO.getSseConnectId());
//        if (printWriter == null) {
//            log.error("{}连接已经失效", sseMsgSendDTO.getSseConnectId());
//            return Result.failed("连接已经失效");
//        }
//        //检验messageNo状态是否正在发送中
////        sysMessageNoticeService.checkMessageNoStatus(sseMsgSendDTO.getMessageNo());
//        String content = sseMessageNoticePO.getContent();
//        List<SseMsgInfoDTO> msgInfos = JSONArray.parseArray(content, SseMsgInfoDTO.class);
//        String id = UUID.randomUUID().toString().replace("-", "");
//        List<SseRespInfoBO> sseRespInfoBOS = msgInfos.stream().map(msgInfo -> {
//            SseRespInfoBO sseRespInfoBO = SseRespInfoBO.builder().data(msgInfo.getMsg()).event(msgInfo.getEvent()).id(id).build();
//            return sseRespInfoBO;
//        }).collect(Collectors.toList());
//        boolean sendFlag = writeSseInfo(sseMsgSendDTO.getSseConnectId(), sseRespInfoBOS, false);
//        boolean updateMessageInfo = sseMessageNoticeService.updateStatus(sseMessageNoticePO, sendFlag);
//        log.info("sse推送消息({})给{}，更新发送成功状态({}), result：{}", sseMsgSendDTO.getMessageNo(), UserInfoThreadLocal.getItCode() ,sendFlag , updateMessageInfo);
//        return sendFlag ? Result.ok() : Result.failed("发送失败");
//
//    }


    @GetMapping("/close")
    public String close(@RequestParam("Authorization") String Authorization) {
        PrintWriter printWriter = responseMap.remove(Authorization);
        if (printWriter == null) {
            return "该用户的sse连接已经关闭了，不能重复关闭";
        }
        printWriter.close();
        return "ok";
    }

    private void writeSseInfo(String token, SseRespInfoBO sseRespInfoBO) {
        writeSseInfo(token, sseRespInfoBO, false);
    }

    private void writeSseInfo(String token, String data) {
        writeSseInfo(token, SseRespInfoBO.builder().data(data).build(), false);

    }

    // 发送数据给客户端
    private void writeSseInfo(String token, SseRespInfoBO sseRespInfoBO, boolean over) {
        PrintWriter writer = responseMap.get(token);
        if (writer == null) {
            log.error("{}, 连接已经被关闭", token);
            return;
        }
        if (sseRespInfoBO == null) {
            log.error("{}没有需要发送的消息", token);
        }
        HashMap<String, String> sseRespInfoMap = JSON.parseObject(JSON.toJSONString(sseRespInfoBO), HashMap.class);
        log.info("sse subscribe token {}, write param sseRespInfoMap:{} ", token, sseRespInfoMap);
        sseRespInfoMap.entrySet().stream().filter(entry -> StringUtils.isNotEmpty(entry.getValue())).forEach(entry -> writer.append(entry.getKey()).append(":").append(entry.getValue()).append("\n"));
        writer.println();
        writer.flush();
        log.info("sse subscribe token {}, writeSseInfo:{} end", token, sseRespInfoBO);
        if (over) {
            responseMap.remove(token);
        }
    }

    private boolean writeSseInfo(String token, List<SseRespInfoBO> sseRespInfoBOS, boolean over) {
        PrintWriter writer = responseMap.get(token);
        if (writer == null) {
            log.error("{}, 连接已经被关闭", token);
            return false;
        }
        if (sseRespInfoBOS == null || sseRespInfoBOS.size() ==0) {
            log.error("{}没有需要发送的消息", token);
            return false;
        }
        for (SseRespInfoBO sseRespInfoBO : sseRespInfoBOS) {
            HashMap<String, String> sseRespInfoMap = JSON.parseObject(JSON.toJSONString(sseRespInfoBO), HashMap.class);
            log.info("sse subscribe token {}, write param sseRespInfoMap:{} ", token, sseRespInfoMap);
            sseRespInfoMap.entrySet().stream().filter(entry -> StringUtils.isNotEmpty(entry.getValue())).forEach(entry -> writer.append(entry.getKey()).append(":").append(entry.getValue()).append("\n"));
            writer.println();
        }
        writer.flush();
        log.info("sse subscribe token {}, writeSseInfo:{} end", token, sseRespInfoBOS);
        if (over) {
            responseMap.remove(token);
        }
        return true;
    }

}
