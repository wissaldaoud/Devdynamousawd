package com.pi.hackathon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private int id;
    private int userId;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Post post;


}
