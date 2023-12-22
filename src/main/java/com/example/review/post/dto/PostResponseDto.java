package com.example.review.post.dto;

import com.example.review.common.CommonResponseDto;
import com.example.review.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto extends CommonResponseDto {

    private long id;
    private String title;
    private String content;
    private String user;
    private LocalDateTime creatAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = post.getUser().getUsername();
        this.creatAt = post.getCreatedAt();
    }
}
