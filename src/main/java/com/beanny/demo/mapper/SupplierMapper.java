package com.beanny.demo.mapper;

import com.beanny.demo.dto.supplier.SupplierDto;
import com.beanny.demo.dto.supplier.SupplierResponseDto;
import com.beanny.demo.dto.supplier.UpdateSupplierDto;
import com.beanny.demo.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupplierMapper {
    public Supplier toEntity(SupplierDto dto) {
        Supplier entity = new Supplier();
        
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRating(dto.getRating());
        
        return entity;
    }
    
    public SupplierResponseDto toDto(Supplier entity) {
        SupplierResponseDto dto = new SupplierResponseDto();
        
        dto.setId(entity.getId());
        dto.setSupplierName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setRating(entity.getRating());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
    
    public List<SupplierResponseDto> toDtoList(List<Supplier> entities) {
        if(entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        
        return entities
                .stream()
                .map(supplier -> this.toDto(supplier))
                .collect(Collectors.toList());
    }
    
    public void updateEntityFromDto(Supplier entity, UpdateSupplierDto dto) {
        if(entity == null || dto == null) {
            return;
        }
        
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setRating(dto.getRating());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
    }
}
