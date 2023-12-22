package com.example.review.post.dto;

import com.example.review.comment.dto.CommentResponseDto;
import com.example.review.comment.entity.Comment;
import com.example.review.common.CommonResponseDto;
import com.example.review.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostCommentResponseDto extends CommonResponseDto {

    private long id;
    private String title;
    private String content;
    private String user;
    private Long accessCount;
    private List<CommentResponseDto> commentResponseDtoListList;

    public PostCommentResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = post.getUser().getUsername();
        this.commentResponseDtoListList = commentListToDtoList(post);
    }


    public List<CommentResponseDto> commentListToDtoList(Post post) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        //코멘트 리스트 반환
        commentList = post.getCommentList();
        //코멘트 리스트를 commentDto리스트로 바꾸기
        commentList.forEach(comment -> {
            var commentDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentDto);
        });
        return commentResponseDtoList;
    }
}
