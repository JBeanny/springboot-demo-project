package com.beanny.demo.service;

import com.beanny.demo.common.config.ApplicationConfiguration;
import com.beanny.demo.dto.base.PaginatedResponse;
import com.beanny.demo.dto.user.ChangePasswordUserDto;
import com.beanny.demo.dto.user.UpdateUserDto;
import com.beanny.demo.dto.user.UserResponseDto;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.exception.model.UnprocessableEntityException;
import com.beanny.demo.mapper.UserMapper;
import com.beanny.demo.repository.UserRepository;
import com.beanny.demo.service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ApplicationConfiguration appConfig;

    public PaginatedResponse listUsersWithPagination(Pageable pageable) {
        Page<User> userPages = userRepository.findAll(pageable);
        Page<UserResponseDto> userPagesDto = userPages.map(user -> mapper.toDto(user));

        return PaginatedResponse.from(userPagesDto,appConfig.getPagination().getUrlByResource("user"));
    }
    
    public List<UserResponseDto> listUsers() {
        List<User> users = userRepository.findAll();
        
        return mapper.toDtoList(users);
    }
    
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
        
        return mapper.toDto(user);
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
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("user not found: " + username);
                });
    }
}