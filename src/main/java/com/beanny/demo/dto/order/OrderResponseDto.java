package com.beanny.demo.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    @JsonProperty("order_id")
    private Long id;
    
    private String status;
    
    private Double total;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    @JsonProperty("items")
    private List<OrderItemResponseDto> items;
}
