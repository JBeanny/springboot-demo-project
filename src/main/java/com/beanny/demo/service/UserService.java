package com.beanny.demo.service;

import com.beanny.demo.dto.user.UserResponseDto;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.UserMapper;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper mapper;
    
    public ResponseEntity<BaseResponseWithDataModel> listUsers() {
        List<User> users = userRepository.findAll();
        
        List<UserResponseDto> dtos = mapper.toDtoList(users);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieve users",dtos));
    }
    
    public ResponseEntity<BaseResponseWithDataModel> getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
        
        UserResponseDto dto = mapper.toDto(user);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","user found",dto));
    }
    
    public ResponseEntity<BaseResponseModel> createUser(UserDto payload) {
        // validate if username is existed
        if(userRepository.existsByName(payload.getName())) {
            throw new DuplicateResourceException("username is already existed");
        }
        
        // validate if email is existed
        if(userRepository.existsByEmail(payload.getEmail())) {
            throw new DuplicateResourceException("email is already existed");
        }
        
        User user = mapper.toEntity(payload);
        
        userRepository.save(user);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully created user"));
    }
    
    public ResponseEntity<BaseResponseModel> updateUser(UserDto payload, Long userId) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + userId));
        
        // modify values
        mapper.updateEntityFromDto(existing,payload);
        
        userRepository.save(existing);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated user"));
    }
    
    public ResponseEntity<BaseResponseModel> deleteUser(Long userId) {
        // if user not found, then response 404
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("user not found with id: " + userId);
        }
        
        // user found , then delete
        userRepository.deleteById(userId);
        
        // 200 OK
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted user"));
    }
}