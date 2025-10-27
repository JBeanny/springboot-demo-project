package com.beanny.demo.service.mail;

import com.beanny.demo.common.config.ApplicationConfiguration;
import com.beanny.demo.common.wrapper.WebClientWrapper;
import com.beanny.demo.entity.Order;
import com.beanny.demo.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private WebClientWrapper webClientWrapper;
    
    @Autowired
    private ApplicationConfiguration appConfig;
    
    @Async
    public void sendOrderConfirmationNotification(Order order) {
        String threadName = Thread.currentThread().getName();
        
        log.info("[ASYNC-NOTIFICATION] Start sending notification to Telegram for Order: {} | Thread: {}", order.getId(), threadName);
        
        try {
            String formattedMsg = this.generateTelegramNotificationTemplate(order);
            this.sendNotification(formattedMsg);
            
            log.info("[ASYNC-NOTIFICATION] Sent notification to Telegram successfully for Order: {} | Thread: {}",order.getId(),threadName);
        } catch (Exception e) {
            log.error("[ASYNC-NOTIFICATION] Failed to send notification for Order: {} | Error: {}",order.getId(),e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    private void sendNotification(String content) {
        String url = appConfig.getTelegram().getBaseUrl();
        String chatId = appConfig.getTelegram().getChatId();
        
        Map<String,String> payload = new HashMap<>();
        payload.put("chat_id", chatId);
        payload.put("text", content);
        payload.put("parse_mode", "HTML");
        
        webClientWrapper.postSync(url,payload, Object.class);
    }
    
    private String generateTelegramNotificationTemplate(Order order) {
        StringBuilder message = new StringBuilder();
        int itemCount = order.getItems() != null ? order.getItems().size() : 0;
        
        message.append("<b>🔔 New Order Received</b>\n");
        message.append("Order ID: #").append(order.getId()).append("\n");
        message.append("Status: ").append(order.getStatus()).append("\n");
        message.append("Ordered at: ").append(order.getCreatedAt()).append("\n");
        message.append("Items: <i>").append(itemCount).append("</i>\n");
        
        if(itemCount != 0) {
            message.append("\n<b>Order Details: </b>\n");
            
            for(OrderItem item : order.getItems()) {
                message
                        .append("  - Product ID: ").append(item.getProductId())
                        .append("  (Qty= ").append(item.getQuantity()).append(")\n");
            }
        } else {
            message.append("<i>No items</i>\n\n");
        }
        
        message.append("<b>⚡ Action required: check the order process</b>");
        
        return message.toString();
    }
}
