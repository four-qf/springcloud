package com.qx.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @author qiux
 * @Date 2023/3/3
 * @since
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    private static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    @GetMapping(value = "/sub")
    public SseEmitter substric(String id) {
        SseEmitter sseEmitter = new SseEmitter();
        try {
            sseEmitter.send(SseEmitter.event().id(id).data("订阅成功").name("connection").reconnectTime(60*60*30));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitterMap.put(id, sseEmitter);
        return sseEmitter;
    }

    @GetMapping(value = "/send")
    public String send(String id, String msg) throws IOException {
        SseEmitter sseEmitter = sseEmitterMap.get(id);
        if (sseEmitter == null) {
            return "连接已断开";
        }
        sseEmitter.send(SseEmitter.event().id(id).data(msg).name("msg"));
        return "ok";
    }

}
