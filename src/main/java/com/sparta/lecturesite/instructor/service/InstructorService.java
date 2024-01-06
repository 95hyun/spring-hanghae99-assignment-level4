package com.sparta.lecturesite.instructor.service;

import com.sparta.lecturesite.instructor.dto.InstructorRequestDto;
import com.sparta.lecturesite.instructor.dto.InstructorResponseDto;
import com.sparta.lecturesite.instructor.entity.Instructor;
import com.sparta.lecturesite.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;

    // 강사 등록
    public InstructorResponseDto createInstructor(InstructorRequestDto requestDto) {
        Instructor instructor = new Instructor(requestDto);
        Instructor saveInstructor = instructorRepository.save(instructor);
        return new InstructorResponseDto(saveInstructor);
    }
}
