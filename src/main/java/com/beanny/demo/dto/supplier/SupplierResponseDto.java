package com.beanny.demo.dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({"supplier_id","supplier_name","address","rating","contact_number","email","created_at","updated_at"})
public class SupplierResponseDto {
    @JsonProperty("supplier_id")
    private Long id;
    
    @JsonProperty("supplier_name")
    private String supplierName;
    
    private String address;
    
    private String rating;
    
    @JsonProperty("contact_number")
    private String phone;
    
    private String email;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
