package com.beanny.demo.controller;

import com.beanny.demo.common.ResponseWrapper;
import com.beanny.demo.dto.base.Response;
import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderResponseDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for order management operations.
 * This controller handles HTTP concerns and delegates business logic to the service layer.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Retrieves all orders in the system.
     *
     * @return ResponseEntity containing list of orders
     */
    @GetMapping
    public ResponseEntity<Response<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseWrapper.ok("Orders retrieved successfully", orders);
    }
    
    /**
     * Retrieves a specific order by ID.
     *
     * @param orderId The order ID to retrieve
     * @return ResponseEntity containing the order details
     */
    @GetMapping("/{order_id}")
    public ResponseEntity<Response<OrderResponseDto>> getOrderById(@PathVariable("order_id") Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseWrapper.ok("Order retrieved successfully", order);
    }
    
    /**
     * Creates a new order.
     *
     * @param orderDto Order creation request
     * @return ResponseEntity containing the created order
     */
    @PostMapping
    public ResponseEntity<Response<OrderResponseDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderResponseDto createdOrder = orderService.createOrder(orderDto);
        return ResponseWrapper.created("Order created successfully", createdOrder);
    }
    
    /**
     * Updates the status of an existing order.
     *
     * @param orderId The order ID to update
     * @param updateDto Update request containing new status
     * @return ResponseEntity containing the updated order
     */
    @PatchMapping("/{order_id}")
    public ResponseEntity<Response<OrderResponseDto>> updateOrderStatus(
            @PathVariable("order_id") Long orderId,
            @Valid @RequestBody OrderUpdateDto updateDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, updateDto);
        return ResponseWrapper.ok("Order status updated successfully", updatedOrder);
    }
    
    /**
     * Deletes an order by ID.
     *
     * @param orderId The order ID to delete
     * @return ResponseEntity with confirmation message
     */
    @DeleteMapping("/{order_id}")
    public ResponseEntity<Response<Void>> deleteOrder(@PathVariable("order_id") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseWrapper.ok("Order deleted successfully");
    }
}
