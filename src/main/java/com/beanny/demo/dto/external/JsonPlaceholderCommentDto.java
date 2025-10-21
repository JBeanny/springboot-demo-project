package com.beanny.demo.dto.external;

import lombok.Data;

@Data
public class JsonPlaceholderCommentDto {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;
}
