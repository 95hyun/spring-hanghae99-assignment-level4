package com.sparta.lecturesite.lecture.likes.repository;

import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.lecture.likes.entity.Likes;
import com.sparta.lecturesite.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByLectureAndUser(Lecture lecture, User user);
}
