package com.sparta.lecturesite.lecture.repository;

import com.sparta.lecturesite.lecture.dto.LectureResponseDto;
import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.lecture.entity.LectureCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findAllByCategory(LectureCategoryEnum category);
}
