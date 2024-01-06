package com.sparta.lecturesite.lecture.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String email;
    private LocalDateTime createdAt;

    @Builder
    public CommentResponseDto (Long id, String content, String email, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.email = email;
        this.createdAt = createdAt;
    }
}
