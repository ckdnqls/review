package com.example.review.post.entity;

import com.example.review.comment.entity.Comment;
import com.example.review.common.Timestamped;
import com.example.review.post.dto.PostRequestDto;
import com.example.review.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "user_name")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @OneToMany (mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();

    @Column(length = 500)
    private String title;

    @Column(length = 5000)
    private String content;

    public Post(PostRequestDto dto, User user){
        this.user = user;
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }



    public void updatePost(PostRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

}
