package com.beanny.demo.mapper;

import com.beanny.demo.dto.order.OrderItemDto;
import com.beanny.demo.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItem toEntity(OrderItemDto dto) {
        OrderItem entity = new OrderItem();
        
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getAmount());
        
        return entity;
    }
}
