package com.example.review.comment.controller;

import com.example.review.comment.dto.CommentRequestDto;
import com.example.review.comment.dto.CommentResponseDto;
import com.example.review.comment.service.CommentService;
import com.example.review.user.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/posts/{postId}")

@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, requestDto, userDetails.getUser());
    }

    @PutMapping("/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }

}
