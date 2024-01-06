package com.sparta.lecturesite.lecture.entity;

import com.sparta.lecturesite.instructor.entity.Instructor;
import com.sparta.lecturesite.lecture.comment.entity.Comment;
import com.sparta.lecturesite.lecture.dto.LectureRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Lecture extends LectureDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lectureName;
    private long price;
    private String description;
    private long likeCount = 0;

    @Enumerated(value = EnumType.STRING)
    private LectureCategoryEnum category; // SPRING, REACT, NODE

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Lecture(LectureRequestDto requestDto, Instructor instructor) {
        this.lectureName = requestDto.getLectureName();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
        this.instructor = instructor;
    }

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public void subLikeCount() {
        this.likeCount -= 1;
    }
}
