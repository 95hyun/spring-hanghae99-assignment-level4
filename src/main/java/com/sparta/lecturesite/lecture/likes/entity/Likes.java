package com.sparta.lecturesite.lecture.likes.entity;

import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;


    public Likes(Lecture lecture, User user) {
        this.lecture = lecture;
        this.user = user;
    }
}
