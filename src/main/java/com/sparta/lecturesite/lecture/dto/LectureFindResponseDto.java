package com.sparta.lecturesite.lecture.dto;

import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.lecture.entity.LectureCategoryEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LectureFindResponseDto {

    // 강의 정보
    private Long id;
    private String lectureName;
    private long price;
    private String description;
    private LectureCategoryEnum category;
    private LocalDate registrationDate;

    // 강사 정보
    private String name;
    private int experienceYears;
    private String company;
    private String introduction;

    public LectureFindResponseDto(Lecture lecture) {
        this.id = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.price = lecture.getPrice();
        this.description = lecture.getDescription();
        this.category = lecture.getCategory();
        this.registrationDate = lecture.getRegistrationDate();

        this.name = lecture.getInstructor().getName();
        this.experienceYears = lecture.getInstructor().getExperienceYears();
        this.company = lecture.getInstructor().getCompany();
        this.introduction = lecture.getInstructor().getIntroduction();

    }
}
