package com.sparta.lecturesite.instructor.dto;

import com.sparta.lecturesite.instructor.entity.Instructor;
import lombok.Getter;

@Getter
public class InstructorResponseDto {
    private Long id;
    private String name;
    private int experienceYears;
    private String company;
    private String phoneNumber;
    private String introduction;


    public InstructorResponseDto(Instructor saveInstructor) {
        this.id = saveInstructor.getId();
        this.name = saveInstructor.getName();
        this.experienceYears = saveInstructor.getExperienceYears();
        this.company = saveInstructor.getCompany();
        this.phoneNumber = saveInstructor.getPhoneNumber();
        this.introduction = saveInstructor.getIntroduction();
    }
}
