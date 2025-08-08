package com.beanny.demo.mapper;

import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.entity.Order;
import com.beanny.demo.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    public Order toEntity(OrderDto dto) {
        Order entity = new Order();
        
        // order item entities
        List<OrderItem> orderItemEntities = dto.getOrderItems()
                .stream()
                .map(orderItemDto -> {
                    OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
                    orderItem.setOrder(entity);
                    
                    return orderItem;
                })
                .toList();
        
        entity.setItems(orderItemEntities);
        
        return entity;
    }
    
}
