package com.sparta.lecturesite.lecture.service;

import com.sparta.lecturesite.instructor.entity.Instructor;
import com.sparta.lecturesite.instructor.repository.InstructorRepository;
import com.sparta.lecturesite.lecture.comment.dto.CommentRequestDto;
import com.sparta.lecturesite.lecture.comment.dto.CommentResponseDto;
import com.sparta.lecturesite.lecture.comment.entity.Comment;
import com.sparta.lecturesite.lecture.comment.repository.CommentRepository;
import com.sparta.lecturesite.lecture.dto.LectureFindResponseDto;
import com.sparta.lecturesite.lecture.dto.LectureRequestDto;
import com.sparta.lecturesite.lecture.dto.LectureResponseDto;
import com.sparta.lecturesite.lecture.entity.Lecture;
import com.sparta.lecturesite.lecture.entity.LectureCategoryEnum;
import com.sparta.lecturesite.lecture.likes.entity.Likes;
import com.sparta.lecturesite.lecture.likes.repository.LikeRepository;
import com.sparta.lecturesite.lecture.repository.LectureRepository;
import com.sparta.lecturesite.user.entity.User;
import com.sparta.lecturesite.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 강의 등록
    @Transactional
    public LectureResponseDto createLecture(LectureRequestDto requestDto) {
        Instructor instructor = instructorRepository.findByName(requestDto.getName()).orElseThrow(()
                -> new EntityNotFoundException("입력된 강사는 등록된 강사가 아닙니다."));
        Lecture lecture = new Lecture(requestDto, instructor);
        Lecture saveLecture = lectureRepository.save(lecture);
        return new LectureResponseDto(saveLecture);
    }

    // 선택한 강의 조회 - 강의를 촬영한 강사 정보까지 확인
    @Transactional(readOnly = true)
    public LectureFindResponseDto findLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(()
                -> new EntityNotFoundException("강의를 찾을 수 없습니다."));
        return new LectureFindResponseDto(lecture);
    }

    // 카테고리별 강의 목록 조회 기능 - 선택 기준에따른 정렬 방식
    @Transactional(readOnly = true)
    public List<LectureResponseDto> getLecturesByCategory(LectureCategoryEnum category, String selection, String orderby) {
        List<LectureResponseDto> lectures = lectureRepository.findAllByCategory(category).stream().map(LectureResponseDto::new).toList();

        // 기준에 따른 정렬 Comparator 설정
        Comparator<LectureResponseDto> comparator = null;

        // 선택한 기준(selection)에 따라 Comparator를 설정
        switch (selection) {
            case "lectureName":
                comparator = Comparator.comparing(LectureResponseDto::getLectureName);
                break;
            case "price":
                comparator = Comparator.comparing(LectureResponseDto::getPrice);
                break;
            case "registrationDate":
                comparator = Comparator.comparing(LectureResponseDto::getRegistrationDate);
                break;
            default:
                // 선택된 기준이 없을 경우, 기본적으로 강의명을 기준으로 설정
                comparator = Comparator.comparing(LectureResponseDto::getLectureName);
                break;
        }
        switch (orderby) {
            case "asc" : // 오름차순
                break;
            case "desc" : // 내림차순
                comparator = comparator.reversed();
                break;
        }
        // 정렬된 강의 목록 반환
        return lectures.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // 선택한 강의 댓글 등록
    @Transactional
    public CommentResponseDto addCommentToLecture(Long lectureId, CommentRequestDto commentRequestDto, String username) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다. ID: " + lectureId));
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + username));

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .user(user)
                .lecture(lecture)
                .build();

        commentRepository.save(comment);

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .email(comment.getUser().getEmail())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // 선택한 강의의 선택한 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long lectureId, Long commentID, CommentRequestDto requestDto, String username) {

        // 해당 댓글이 유효한지?
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

        // 해당 강의 id 유효한지?
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("해당 강의가 존재하지 않습니다."));

        // 사용자 체크
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (!comment.getUser().getEmail().equals(username)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        // 댓글 수정
        comment.update(requestDto.getContent(), lecture, user);

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .email(comment.getUser().getEmail())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // 선택한 강의의 선택한 댓글 삭제
    @Transactional
    public void deleteComment(Long lectureId, Long commentID, String username) {
        // 해당 댓글이 유효한지?
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

        // 해당 강의 id 유효한지?
        lectureRepository.findById(lectureId).orElseThrow(() -> new NoSuchElementException("해당 강의가 존재하지 않습니다."));

        if (!comment.getUser().getEmail().equals(username)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    public boolean likeOrUnLikeLecture(long lectureId, String username) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("해당 강의가 존재하지 않습니다."));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 좋아요가 이미 존재하는지?
        Likes alreadyLike = likeRepository.findByLectureAndUser(lecture, user);

        // 좋아요가 이미 있으면 삭제
        if (alreadyLike != null) {
            alreadyLike.getLecture().subLikeCount();
            likeRepository.delete(alreadyLike);
            return false;
        }

        // 좋아요 등록
        Likes likes = new Likes(lecture, user);
        likes.getLecture().addLikeCount();
        likeRepository.save(likes);
        return true;
    }
}
