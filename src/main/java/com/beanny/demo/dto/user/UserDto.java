package com.beanny.demo.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String address;
    private String email;
    private String role = "USER";
}
