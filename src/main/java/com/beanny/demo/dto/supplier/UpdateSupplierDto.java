package com.beanny.demo.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSupplierDto {
    @NotNull(message = "supplier name is required")
    @NotBlank(message = "supplier name must not be blank")
    @Size(min = 5, max = 50, message = "supplier name must be between 5 and 100 characters")
    private String name;
    
    @Size(max = 255, message = "address must not exceed 255 characters")
    private String address;
    
    @Size(max = 50, message = "rating must not exceed 50 characters")
    private String rating;
    
    @Size(max = 14, message = "phone number must not exceed 14 characters")
    private String phone;
    
    @Email(message = "email should be valid")
    @Size(max = 50,message = "email must not exceed 50 characters")
    private String email;
}
