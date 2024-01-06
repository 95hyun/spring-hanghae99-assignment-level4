package com.sparta.lecturesite.instructor.entity;

import com.sparta.lecturesite.instructor.dto.InstructorRequestDto;
import com.sparta.lecturesite.lecture.entity.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int experienceYears;
    private String company;
    private String phoneNumber;
    private String introduction;

    public Instructor(InstructorRequestDto requestDto) {
        this.name = requestDto.getName();
        this.experienceYears = requestDto.getExperienceYears();
        this.company = requestDto.getCompany();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.introduction = requestDto.getIntroduction();
    }
}
