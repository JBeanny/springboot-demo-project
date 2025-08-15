package com.beanny.demo.event;

import com.beanny.demo.dto.order.OrderResponseDto;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when an order is successfully created.
 * This enables other services to react to order creation without tight coupling.
 */
public class OrderCreatedEvent extends ApplicationEvent {
    
    private final OrderResponseDto orderResponse;
    private final String eventId;
    private final Long timestamp;
    
    public OrderCreatedEvent(Object source, OrderResponseDto orderResponse) {
        super(source);
        this.orderResponse = orderResponse;
        this.eventId = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }
    
    public OrderResponseDto getOrderResponse() {
        return orderResponse;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
}