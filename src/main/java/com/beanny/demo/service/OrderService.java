package com.beanny.demo.service;

import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderItemDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.entity.Order;
import com.beanny.demo.entity.Stock;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.exception.model.UnprocessableEntityException;
import com.beanny.demo.mapper.OrderMapper;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.repository.OrderRepository;
import com.beanny.demo.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderMapper mapper;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    public ResponseEntity<BaseResponseWithDataModel> listOrders() {
        List<Order> orders = orderRepository.findAll();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved orders",mapper.toResponseDtoList(orders)));
    }
    
    @Transactional
    public ResponseEntity<BaseResponseModel> createOrder(OrderDto payload) {
        // map for product ids
        List<Long> productIds = payload.getOrderItems().stream()
                .map(OrderItemDto::getProductId)
                .toList();
        
        // get stocks in productIds
        List<Stock> stocks = stockRepository.findByProductIdIn(productIds, Sort.by(Sort.Direction.ASC, "createdAt"));
        
        // map for required quantity of productIds
        // example: 1: 100, 2: 50
        Map<Long,Integer> requiredQuantities = payload.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemDto::getProductId,OrderItemDto::getAmount));
        
        // deduct stock for each product
        // [1,3]
        for(Long productId : requiredQuantities.keySet()) {
            // quantity to deduct
            int remain = requiredQuantities.get(productId);
            
            // filter stocks by product id
            List<Stock> stocksByProduct = stocks.stream()
                    .filter(stock -> stock.getProduct().getId().equals(productId))
                    .toList();
            
            // calculate and compare qty
            for(Stock stock : stocksByProduct) {
                if(remain <= 0) break;
                
                int available = stock.getQuantity();
                
                if(available >= remain) {
                    stock.setQuantity(available - remain);
                    remain = 0;
                } else {
                    stock.setQuantity(0);
                    remain -= available;
                }
            }
            
            // not enough qty for sale
            if(remain > 0) {
                throw new UnprocessableEntityException("Not enough stock for product id: " + productId);
            }
        }
        
        // save updated stocks to DB
        stockRepository.saveAll(stocks);
        
        // create order entity
        Order order = mapper.toEntity(payload);

        orderRepository.save(order);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully placed order"));
    }
    
    public ResponseEntity<BaseResponseModel> updateOrderStatus(Long orderId, OrderUpdateDto payload) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("order not found with id: " + orderId);
                });
        
        mapper.updateEntityFromDto(existingOrder,payload);
        orderRepository.save(existingOrder);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated order status: " + payload.getStatus()));
    }
    
    public ResponseEntity<BaseResponseModel> deleteOrder(Long orderId) {
        if(!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("order not found with id: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted order: " + orderId));
    }
}
