package com.beanny.demo.controller;

import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public ResponseEntity<BaseResponseWithDataModel> listOrders() {
        return orderService.listOrders();
    }
    
    @PostMapping
    public ResponseEntity<BaseResponseModel> placeOrder(@Valid @RequestBody OrderDto payload) {
        return orderService.createOrder(payload);
    }
    
    @PatchMapping("/{order_id}")
    public ResponseEntity<BaseResponseModel> updateOrderStatus(@PathVariable("order_id") Long orderId,@Valid @RequestBody OrderUpdateDto payload) {
        return orderService.updateOrderStatus(orderId,payload);
    }
    
    @DeleteMapping("/{order_id}")
    public ResponseEntity<BaseResponseModel> deleteOrder(@PathVariable("order_id") Long orderId) {
        return orderService.deleteOrder(orderId);
    }
}
