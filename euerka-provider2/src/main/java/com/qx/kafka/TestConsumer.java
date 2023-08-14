package com.qx.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author qiux
 * @Date 2023/6/23
 * @since
 */
@Slf4j
@Configuration
public class TestConsumer {

    @KafkaListener(topics = {"test.group"}, groupId = "test-qx")
    public void deal(String message) {
        log.info("topic:test.group,group:test-qx, message:{}", message);
    }

    @KafkaListener(topics = {"test.group"}, groupId = "test-qx1")
    public void deal2(String message) {
        log.info("topic:test.group,group:test-qx1, message:{}", message);
    }

}
