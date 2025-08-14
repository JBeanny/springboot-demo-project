package com.beanny.demo.dto.order;

import com.beanny.demo.common.annotations.ValidEnum;
import com.beanny.demo.common.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderUpdateDto {
    @JsonProperty("status")
    @ValidEnum(enumClass = OrderStatus.class, message = "Value must be one of PENDING,FAILED,SUCCESS")
    private String status;
}
