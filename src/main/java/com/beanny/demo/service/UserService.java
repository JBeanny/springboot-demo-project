package com.beanny.demo.service;

import com.beanny.demo.entity.User;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.model.UserModel;
import com.beanny.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<BaseResponseWithDataModel> listUsers() {
        List<User> userData = userRepository.findAll();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieve users",userData));
    }
    
    public ResponseEntity<BaseResponseWithDataModel> getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseWithDataModel("fail","user not found with id : " + userId,null));
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","user found",user.get()));
    }
    
    public ResponseEntity<BaseResponseModel> createUser(UserModel payload) {
        User user = new User();
        user.setName(payload.getName());
        user.setAddress(payload.getAddress());
        user.setAge(payload.getAge());
        user.setEmail(payload.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(payload.getRole());
        
        userRepository.save(user);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully created user"));
    }
    
    public ResponseEntity<BaseResponseModel> updateUser(UserModel payload, Long userId) {
        Optional<User> existing = userRepository.findById(userId);
        
        // if user not found, then response 404
        if(existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseModel("fail","user not found with id: " + userId));
        }
        
        // modify values
        User updatedUser = existing.get();
        
        updatedUser.setEmail(payload.getEmail());
        updatedUser.setName(payload.getName());
        updatedUser.setAge(payload.getAge());
        updatedUser.setAddress(payload.getAddress());
        updatedUser.setRole(payload.getRole());
        
        userRepository.save(updatedUser);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated user"));
    }
    
    public ResponseEntity<BaseResponseModel> deleteUser(Long userId) {
        // if user not found, then response 404
        if(!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseModel("fail","user not found with id: " + userId));
        }
        
        // user found , then delete
        userRepository.deleteById(userId);
        
        // 200 OK
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted user"));
    }
}