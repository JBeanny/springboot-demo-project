package com.beanny.demo.service;

import com.beanny.demo.common.config.ApplicationConfiguration;
import com.beanny.demo.dto.base.PaginatedResponse;
import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderResponseDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.entity.Order;
import com.beanny.demo.event.model.OrderEvent;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.OrderMapper;
import com.beanny.demo.repository.OrderRepository;
import com.beanny.demo.repository.StockRepository;
import com.beanny.demo.service.kafka.ProducerService;
import com.beanny.demo.service.mail.NotificationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderMapper mapper;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private StockManagementService stockManagementService;

    @Autowired
    private ApplicationConfiguration appConfig;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ProducerService<OrderEvent> producerService;

    public PaginatedResponse listOrdersWithPagination(Pageable pageable) {
        Page<Order> orderPages = orderRepository.findAll(pageable);
        Page<OrderResponseDto> orderPagesDto = orderPages.map(order -> mapper.toResponseDto(order));

        return PaginatedResponse.from(orderPagesDto,appConfig.getPagination().getUrlByResource("order"));
    }
    
    public List<OrderResponseDto> listOrders() {
        List<Order> orders = orderRepository.findAll();
        
        return mapper.toResponseDtoList(orders);
    }
    
    @Transactional
    public void createOrder(OrderDto payload) {
        String threadName = Thread.currentThread().getName();
        
        log.info("[SYNC-ORDER] Creating new order | Thread: {}", threadName);
        // reserve stock for order
        stockManagementService.reserveStockForOrder(payload.getOrderItems());
        
        // create order entity
        Order order = mapper.toEntity(payload);

        orderRepository.save(order);
        
        producerService.sendMessage("order.event", new OrderEvent(order.getId()));
        
        log.info("[SYNC-ORDER] Order created successfully with Order: {} | Thread: {}",order.getId(), threadName);
        log.info("[SYNC-ORDER] Trigger send notification asynchronously for Order: {} | Thread: {}",order.getId(), threadName );
        
        notificationService.sendOrderConfirmationNotification(order);
        
        log.info("[SYNC-ORDER-COMPLETED] Completed order and triggered send notification");
    }
    
    public OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto payload) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("order not found with id: " + orderId);
                });
        
        mapper.updateEntityFromDto(existingOrder,payload);
        orderRepository.save(existingOrder);
        
        return mapper.toResponseDto(existingOrder);
    }
    
    public void deleteOrder(Long orderId) {
        if(!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("order not found with id: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
    }
}
