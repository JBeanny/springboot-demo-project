package com.beanny.demo.dto.external;

import lombok.Data;

@Data
public class JsonPlaceholderPostDto {
    private int userId;
    private int id;
    private String title;
    private String body;
}