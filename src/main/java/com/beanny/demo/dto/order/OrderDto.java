package com.beanny.demo.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    @JsonProperty("items")
    @NotNull(message = "order item is required")
    @NotEmpty(message = "order item cannot be empty")
    private List<OrderItemDto> orderItems;
}
