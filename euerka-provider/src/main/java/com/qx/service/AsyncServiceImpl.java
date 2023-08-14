package com.qx.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @author qiux
 * @Date 2023/3/28
 * @since
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService{



    @Async
    public void testSync(){
        log.info("--------testsync");
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        log.info("--------testsync");
        throw new RuntimeException("测试异常");
    }

}
