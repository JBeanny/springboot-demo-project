package com.beanny.demo.controller;

import com.beanny.demo.dto.base.PaginatedResponse;
import com.beanny.demo.dto.base.Response;
import com.beanny.demo.dto.user.ChangePasswordUserDto;
import com.beanny.demo.dto.user.UpdateUserDto;
import com.beanny.demo.dto.user.UserResponseDto;
import com.beanny.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/paginated")
    public ResponseEntity<Response> listUsersWithPagination(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PaginatedResponse<UserResponseDto> users  = userService.listUsersWithPagination(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully retrieved users with pagination",users));
    }
    
    // used for retrieving records
    @GetMapping
    public ResponseEntity<Response> listUsers() {
        List<UserResponseDto> users = userService.listUsers();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully retrieve users",users));
    }
    
    @GetMapping("/{user_id}")
    public ResponseEntity<Response> getUser(@PathVariable("user_id") Long userId) {
        UserResponseDto user = userService.getUser(userId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","user found",user));
    }
    
    //  endpoint -> /api/v1/users/923482348284
    @PutMapping("/{user_id}")
    public ResponseEntity<Response> updateUser(@PathVariable("user_id") Long userId,@Valid @RequestBody UpdateUserDto payload) {
        userService.updateUser(payload,userId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("success","successfully updated user"));
    }
    
    // Path variable
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("success","successfully deleted user"));
    }
    
    // change password
    @PatchMapping("/{user_id}/change-password")
    public ResponseEntity<Response> changePassword(@PathVariable("user_id") Long userId,@RequestBody ChangePasswordUserDto payload) {
        userService.changePassword(payload, userId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("success","successfully changed password"));
    }
}
