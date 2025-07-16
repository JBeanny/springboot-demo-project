package com.beanny.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    private Integer age;
    private String address;
    private String email;
    private String role = "USER";
}
