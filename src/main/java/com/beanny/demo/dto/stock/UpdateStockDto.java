package com.beanny.demo.dto.stock;

import lombok.Data;

@Data
public class UpdateStockDto {
    private Integer operationType; // 1 -> add, 2 -> remove
    private Integer quantity;
}
