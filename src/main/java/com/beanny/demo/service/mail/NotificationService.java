package com.beanny.demo.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    
    @Async
    public void sendOrderConfirmationNotification(Long orderId, String text) {
        String threadName = Thread.currentThread().getName();
        
        log.info("[ASYNC-NOTIFICATION] Start sending notification to Telegram for Order: {} | Thread: {}", orderId, threadName);
        
        try {
            // TODO: send notification to Telegram
            Thread.sleep(10000); // 10000 = 10s
            
            log.info("[ASYNC-NOTIFICATION] Sent notification to Telegram successfully for Order: {} | Thread: {}",orderId,threadName);
        } catch (InterruptedException e) {
            log.error("[ASYNC-NOTIFICATION] Failed to send notification for Order: {} | Error: {}",orderId,e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
