package com.example.review.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private static final int TITLE_MAX_LENGTH = 500;
    private static final int CONTENT_MAX_LENGTH = 5000;
    private String title;
    private String content;
}
