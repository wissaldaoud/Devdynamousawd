package com.pi.hackathon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private int id;
    private int userId;
    private String content;
    private PostType postType;
    private String tag;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<Comment> comments;
    private List<Like> likes;
}
