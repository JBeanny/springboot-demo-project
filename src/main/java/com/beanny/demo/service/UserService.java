package com.beanny.demo.service;

import com.beanny.demo.dto.user.ChangePasswordUserDto;
import com.beanny.demo.dto.user.UpdateUserDto;
import com.beanny.demo.dto.user.UserResponseDto;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.exception.model.UnprocessableEntityException;
import com.beanny.demo.mapper.UserMapper;
import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.repository.UserRepository;
import com.beanny.demo.service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper mapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public List<UserResponseDto> listUsers() {
        List<User> users = userRepository.findAll();
        
        return mapper.toDtoList(users);
    }
    
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
        
        String token = jwtUtil.generateToken(user);
        
        System.out.println("Token: " + token);
        
        return mapper.toDto(user);
    }
    
    public void createUser(UserDto payload) {
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
    }
    
    public void updateUser(UpdateUserDto payload, Long userId) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + userId));
        
        // modify values
        mapper.updateEntityFromDto(existing,payload);
        
        userRepository.save(existing);
    }
    
    public void deleteUser(Long userId) {
        // if user not found, then response 404
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("user not found with id: " + userId);
        }
        
        // user found , then delete
        userRepository.deleteById(userId);
    }
    
    public void changePassword(ChangePasswordUserDto payload, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + userId));
        
        // old password is incorrect
        if(!Objects.equals(user.getPassword(), payload.getOldPassword())) {
            throw new UnprocessableEntityException("old password is incorrect, please enter the correct password");
        }
        
        // new password and confirm password not match
        if(!Objects.equals(payload.getNewPassword(), payload.getConfirmPassword())) {
            throw new UnprocessableEntityException("new password and confirm password must be the same");
        }
        
        mapper.updateEntityChangePassword(user, payload.getNewPassword());
        userRepository.save(user);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("user not found: " + username);
                });
    }
}