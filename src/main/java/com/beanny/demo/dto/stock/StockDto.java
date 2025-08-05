package com.beanny.demo.dto.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockDto {
    @NotNull(message = "product id is required")
    private Long productId;
    
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be atleast 1")
    private Integer quantity;
}
