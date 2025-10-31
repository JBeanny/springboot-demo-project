package com.beanny.demo.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService<T> {
    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;
    
    public void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message)
                .whenComplete((result,error) -> {
                    if(error == null) {
                        log.info("[SUCCESS] Sent message to topic [{}] : {}", topic, message);
                    } else {
                        log.error("[FAILED] Failed to send message to topic [{}]: {}", topic, error.getMessage());
                    }
                });
    }
}
