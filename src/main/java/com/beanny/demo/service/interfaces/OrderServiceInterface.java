package com.beanny.demo.service.interfaces;

import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderResponseDto;
import com.beanny.demo.dto.order.OrderUpdateDto;

import java.util.List;

/**
 * Interface defining the contract for order management operations.
 * This interface promotes loose coupling and enables easy testing and mocking.
 */
public interface OrderServiceInterface {
    
    /**
     * Retrieves all orders from the system.
     *
     * @return List of order response DTOs
     */
    List<OrderResponseDto> getAllOrders();
    
    /**
     * Retrieves a specific order by ID.
     *
     * @param orderId The order ID to retrieve
     * @return Order response DTO
     */
    OrderResponseDto getOrderById(Long orderId);
    
    /**
     * Creates a new order after validating and reserving stock.
     *
     * @param orderDto Order creation request
     * @return Created order response DTO
     */
    OrderResponseDto createOrder(OrderDto orderDto);
    
    /**
     * Updates the status of an existing order.
     *
     * @param orderId Order ID to update
     * @param updateDto Update request containing new status
     * @return Updated order response DTO
     */
    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto updateDto);
    
    /**
     * Deletes an order by ID.
     *
     * @param orderId Order ID to delete
     */
    void deleteOrder(Long orderId);
    
    /**
     * Checks if an order exists.
     *
     * @param orderId Order ID to check
     * @return true if order exists, false otherwise
     */
    boolean orderExists(Long orderId);
}