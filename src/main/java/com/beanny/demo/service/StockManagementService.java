package com.beanny.demo.service;

import com.beanny.demo.dto.order.OrderItemDto;
import com.beanny.demo.entity.Stock;
import com.beanny.demo.exception.model.UnprocessableEntityException;
import com.beanny.demo.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for stock management operations.
 * Provides methods for stock validation, reservation, and inventory updates.
 * This service encapsulates all stock-related business logic and can be used by other services.
 */
@Service
public class StockManagementService {
    
    @Autowired
    private StockRepository stockRepository;
    
    /**
     * Validates and reserves stock for the given order items.
     * This method checks availability and deducts stock quantities in a transactional manner.
     *
     * @param orderItems List of order items to process
     * @throws UnprocessableEntityException if insufficient stock is available
     */
    @Transactional
    public void reserveStockForOrder(List<OrderItemDto> orderItems) {
        List<Long> productIds = orderItems.stream()
                .map(OrderItemDto::getProductId)
                .toList();
        
        // Get all relevant stocks ordered by creation date (FIFO)
        List<Stock> stocks = stockRepository.findByProductIdIn(productIds, 
                Sort.by(Sort.Direction.ASC, "createdAt"));
        
        // Create map of required quantities per product
        Map<Long, Integer> requiredQuantities = orderItems.stream()
                .collect(Collectors.toMap(
                    OrderItemDto::getProductId, 
                    OrderItemDto::getAmount,
                    Integer::sum // Handle duplicate product IDs by summing quantities
                ));
        
        // Process each product's stock reservation
        for (Map.Entry<Long, Integer> entry : requiredQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer requiredQuantity = entry.getValue();
            
            reserveStockForProduct(productId, requiredQuantity, stocks);
        }
        
        // Save all updated stocks
        stockRepository.saveAll(stocks);
    }
    
    /**
     * Validates if sufficient stock is available for the given order items.
     *
     * @param orderItems List of order items to validate
     * @return true if sufficient stock is available, false otherwise
     */
    public boolean validateStockAvailability(List<OrderItemDto> orderItems) {
        try {
            // Use a read-only check by simulating the reservation without saving
            List<Long> productIds = orderItems.stream()
                    .map(OrderItemDto::getProductId)
                    .toList();
            
            List<Stock> stocks = stockRepository.findByProductIdIn(productIds, 
                    Sort.by(Sort.Direction.ASC, "createdAt"));
            
            Map<Long, Integer> requiredQuantities = orderItems.stream()
                    .collect(Collectors.toMap(
                        OrderItemDto::getProductId, 
                        OrderItemDto::getAmount,
                        Integer::sum
                    ));
            
            // Check availability without modifying stock
            for (Map.Entry<Long, Integer> entry : requiredQuantities.entrySet()) {
                Long productId = entry.getKey();
                Integer requiredQuantity = entry.getValue();
                
                int availableQuantity = stocks.stream()
                        .filter(stock -> stock.getProduct().getId().equals(productId))
                        .mapToInt(Stock::getQuantity)
                        .sum();
                
                if (availableQuantity < requiredQuantity) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the total available quantity for a specific product.
     *
     * @param productId The product ID to check
     * @return Total available quantity
     */
    public int getAvailableQuantity(Long productId) {
        List<Stock> stocks = stockRepository.findByProductId(productId);
        return stocks.stream()
                .mapToInt(Stock::getQuantity)
                .sum();
    }
    
    private void reserveStockForProduct(Long productId, Integer requiredQuantity, List<Stock> allStocks) {
        int remaining = requiredQuantity;
        
        List<Stock> productStocks = allStocks.stream()
                .filter(stock -> stock.getProduct().getId().equals(productId))
                .toList();
        
        for (Stock stock : productStocks) {
            if (remaining <= 0) break;
            
            int available = stock.getQuantity();
            
            if (available >= remaining) {
                stock.setQuantity(available - remaining);
                remaining = 0;
            } else {
                stock.setQuantity(0);
                remaining -= available;
            }
        }
        
        if (remaining > 0) {
            throw new UnprocessableEntityException(
                String.format("Insufficient stock for product ID %d. Required: %d, Available: %d", 
                    productId, requiredQuantity, requiredQuantity - remaining)
            );
        }
    }
}