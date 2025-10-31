package com.beanny.demo.event;

import com.beanny.demo.common.enums.OrderStatus;
import com.beanny.demo.entity.Order;
import com.beanny.demo.event.model.OrderEvent;
import com.beanny.demo.event.model.PaymentEvent;
import com.beanny.demo.repository.OrderRepository;
import com.beanny.demo.service.kafka.ProducerService;
import com.beanny.demo.service.mail.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PaymentConsumer {
    @Autowired
    private ProducerService<PaymentEvent> producerService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @KafkaListener(
            topics = "payment.event",
            groupId = "payment-group",
            containerFactory = "paymentContainerFactory"
    )
    public void consume(PaymentEvent event) {
        log.info("[PAYMENT_EVENT] Processing order event with payment: {}", event);
        
        // update order status base on payment status
        // SUCCESS -> SUCCESS, FAILED -> FAILED
        if(event.getStatus()) {
            Optional<Order> existingOrder = orderRepository.findById(event.getOrderId());
            
            if(existingOrder.isEmpty()) {
                log.error("Order is not found: {}", event.getOrderId());
                return;
            }
            
            Order order = existingOrder.get();
            
            order.setStatus(OrderStatus.SUCCESS.name());
            
            orderRepository.save(order);
            
            return;
        }
    }
}
