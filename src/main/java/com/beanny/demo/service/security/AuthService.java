package com.beanny.demo.service.security;

import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.mapper.UserMapper;
import com.beanny.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper mapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public String register(UserDto payload) {
        // validate if username is existed
        if(userRepository.existsByName(payload.getName())) {
            throw new DuplicateResourceException("username is already existed");
        }
        
        // validate if email is existed
        if(userRepository.existsByEmail(payload.getEmail())) {
            throw new DuplicateResourceException("email is already existed");
        }
        
        User user = mapper.toEntity(payload);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User createdUser = userRepository.save(user);
        String token = jwtUtil.generateToken(createdUser);
        
        return token;
    }
}
