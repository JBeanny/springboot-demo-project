package com.beanny.demo.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotNull(message = "username is required")
    @Size(min = 4, max = 50, message = "username must be between 4 and 50 characters")
    private String name;
    
    @NotNull(message = "age is required")
    @Min(value = 18, message = "age must be atleast 18")
    private Integer age;
    
    @NotNull(message = "address is required")
    @Size(min = 5, max = 50, message = "address must be between 5 and 50 characters")
    private String address;
    
    private String role = "USER";
}
