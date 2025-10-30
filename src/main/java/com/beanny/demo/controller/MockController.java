package com.beanny.demo.controller;

import com.beanny.demo.service.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockController {
    @Autowired
    private KafkaProducer kafkaProducer;
    
    @PostMapping
    public String sendEvent(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        
        return "success";
    }
}
