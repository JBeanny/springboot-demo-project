package com.beanny.demo.service.security;

import com.beanny.demo.dto.auth.AuthDto;
import com.beanny.demo.dto.auth.AuthResponseDto;
import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.mapper.UserMapper;
import com.beanny.demo.repository.UserRepository;
import com.beanny.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    public AuthResponseDto register(UserDto payload) {
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
        String accessToken = jwtUtil.generateToken(createdUser);
        
        return new AuthResponseDto(accessToken,null);
    }
    
    public AuthResponseDto login(AuthDto payload) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getUsername(),payload.getPassword())
        );
        
        UserDetails userDetails = userService.loadUserByUsername(payload.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails);
        
        return new AuthResponseDto(accessToken,null);
    }
}
