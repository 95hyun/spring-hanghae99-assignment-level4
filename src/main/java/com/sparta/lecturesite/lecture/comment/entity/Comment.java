package com.sparta.lecturesite.lecture.comment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends CommentTimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    @Builder
    public Comment (String content, User user, Lecture lecture) {
        this.content = content;
        this.user = user;
        this.lecture = lecture;
    }

    public void update(String content, Lecture lecture, User user) {
        this.content = content;
        this.lecture = lecture;
        this.user = user;
    }
}
