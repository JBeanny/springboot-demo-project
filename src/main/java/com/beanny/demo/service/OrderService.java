package com.beanny.demo.service;

import com.beanny.demo.dto.order.OrderDto;
import com.beanny.demo.dto.order.OrderResponseDto;
import com.beanny.demo.dto.order.OrderUpdateDto;
import com.beanny.demo.entity.Order;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.OrderMapper;
import com.beanny.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for order management business logic.
 * This service focuses purely on business operations and delegates cross-service concerns appropriately.
 */
@Service
public class OrderService {
    
    @Autowired
    private OrderMapper mapper;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private StockManagementService stockManagementService;
    
    /**
     * Retrieves all orders from the system.
     *
     * @return List of order response DTOs
     */
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return mapper.toResponseDtoList(orders);
    }
    
    /**
     * Retrieves a specific order by ID.
     *
     * @param orderId The order ID to retrieve
     * @return Order response DTO
     * @throws ResourceNotFoundException if order is not found
     */
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return mapper.toResponseDto(order);
    }
    
    /**
     * Creates a new order after validating and reserving stock.
     *
     * @param orderDto Order creation request
     * @return Created order response DTO
     */
    @Transactional
    public OrderResponseDto createOrder(OrderDto orderDto) {
        // Validate stock availability first
        if (!stockManagementService.validateStockAvailability(orderDto.getOrderItems())) {
            throw new ResourceNotFoundException("Insufficient stock for one or more items in the order");
        }
        
        // Reserve stock for the order
        stockManagementService.reserveStockForOrder(orderDto.getOrderItems());
        
        // Create and save the order
        Order order = mapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        
        return mapper.toResponseDto(savedOrder);
    }
    
    /**
     * Updates the status of an existing order.
     *
     * @param orderId Order ID to update
     * @param updateDto Update request containing new status
     * @return Updated order response DTO
     * @throws ResourceNotFoundException if order is not found
     */
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto updateDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        mapper.updateEntityFromDto(existingOrder, updateDto);
        Order updatedOrder = orderRepository.save(existingOrder);
        
        return mapper.toResponseDto(updatedOrder);
    }
    
    /**
     * Deletes an order by ID.
     *
     * @param orderId Order ID to delete
     * @throws ResourceNotFoundException if order is not found
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
    }
    
    /**
     * Checks if an order exists.
     *
     * @param orderId Order ID to check
     * @return true if order exists, false otherwise
     */
    public boolean orderExists(Long orderId) {
        return orderRepository.existsById(orderId);
    }
}
