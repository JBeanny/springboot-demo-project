package com.beanny.demo.mapper;

import com.beanny.demo.dto.user.ChangePasswordUserDto;
import com.beanny.demo.dto.user.UpdateUserDto;
import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.dto.user.UserResponseDto;
import com.beanny.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public User toEntity(UserDto dto) {
        User entity = new User();
        
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        entity.setAge(dto.getAge());
        entity.setRole(dto.getRole());
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        
        return entity;
    }
    
    public void updateEntityFromDto(User entity, UpdateUserDto dto) {
        if(entity == null || dto == null) {
            return;
        }
        
        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        entity.setAge(dto.getAge());
        entity.setAddress(dto.getAddress());
    }
    
    public UserResponseDto toDto(User entity) {
        UserResponseDto dto = new UserResponseDto();
        
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setAge(entity.getAge());
        dto.setAddress(entity.getAddress());
        dto.setRole(entity.getRole());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
    
    public List<UserResponseDto> toDtoList(List<User> entities) {
        if(entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        
        return entities.stream()
                .map(user -> this.toDto(user))
                .collect(Collectors.toList());
    }
    
    public void updateEntityChangePassword(User entity, String password) {
        entity.setPassword(password);
    }
}
