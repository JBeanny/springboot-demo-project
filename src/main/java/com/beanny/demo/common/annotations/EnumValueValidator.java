package com.beanny.demo.common.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<ValidEnum, String> {
    private List<String> acceptedValues; // ["USER","ADMIN"]
    
    // occurred when @ValidEnum is initialized
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        acceptedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    
    // validate method
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // return true, because we will let other annotation handles it
        if(value == null) {
            return true;
        }
        
        // user -> USER
        return acceptedValues.contains(value.toUpperCase());
    }
}
