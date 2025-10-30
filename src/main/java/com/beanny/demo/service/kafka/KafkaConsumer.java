package com.beanny.demo.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    
    @KafkaListener(topics = "testing-queue", groupId = "testing-group-id")
    public void consume(String message) {
        log.info("message consumed: {}", message);
    }
}
