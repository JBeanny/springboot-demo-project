package com.beanny.demo.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    
    public void sendMessage(String message) {
        String topic = "testing-queue";
        
        kafkaTemplate.send(topic,message);
        
        log.info("message produced[{}]: {}", topic, message);
    }
}
