package com.beanny.demo.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    @JsonProperty("product_id")
    private Long productId;
    
    @JsonProperty("product_name")
    private String productName;
    
    @JsonProperty("purchase_amount")
    private Integer purchaseAmount;
    
    @JsonProperty("unit_price")
    private Double unitPrice;
}
