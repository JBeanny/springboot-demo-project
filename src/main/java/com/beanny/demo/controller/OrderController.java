package com.beanny.demo.controller;

import com.beanny.demo.dto.base.Response;
import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderResponseDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public ResponseEntity<Response> listOrders() {
        List<OrderResponseDto> orders = orderService.listOrders();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully retrieved orders",orders));
    }
    
    @PostMapping
    public ResponseEntity<Response> placeOrder(@Valid @RequestBody OrderDto payload) {
        orderService.createOrder(payload);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("201","success","successfully created order"));
    }
    
    @PatchMapping("/{order_id}")
    public ResponseEntity<Response> updateOrderStatus(@PathVariable("order_id") Long orderId,@Valid @RequestBody OrderUpdateDto payload) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId,payload);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully updated order",updatedOrder));
    }
    
    @DeleteMapping("/{order_id}")
    public ResponseEntity<Response> deleteOrder(@PathVariable("order_id") Long orderId) {
        orderService.deleteOrder(orderId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("success","successfully deleted order: " + orderId));
    }
}
