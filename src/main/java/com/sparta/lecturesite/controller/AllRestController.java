package com.sparta.lecturesite.controller;

import com.sparta.lecturesite.instructor.dto.InstructorRequestDto;
import com.sparta.lecturesite.instructor.dto.InstructorResponseDto;
import com.sparta.lecturesite.instructor.service.InstructorService;
import com.sparta.lecturesite.lecture.comment.dto.CommentRequestDto;
import com.sparta.lecturesite.lecture.comment.dto.CommentResponseDto;
import com.sparta.lecturesite.lecture.dto.LectureFindResponseDto;
import com.sparta.lecturesite.lecture.dto.LectureRequestDto;
import com.sparta.lecturesite.lecture.dto.LectureResponseDto;
import com.sparta.lecturesite.lecture.entity.LectureCategoryEnum;
import com.sparta.lecturesite.lecture.service.LectureService;
import com.sparta.lecturesite.user.dto.SignupRequestDto;
import com.sparta.lecturesite.user.dto.SignupResponseDto;
import com.sparta.lecturesite.user.entity.UserRoleEnum;
import com.sparta.lecturesite.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AllRestController {

    private final UserService userService;
    private final InstructorService instructorService;
    private final LectureService lectureService;

    @PostMapping("/signup")
    public SignupResponseDto signup(
            @Valid @RequestBody SignupRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException("유효한 형식이 아니어서 회원가입에 실패하였습니다.");
            // 유효성 검사 실패 시 클라이언트에게 에러 응답
        }
        return userService.signup(requestDto);
    }

    /**
     * 강사 등록 (post)
     * @Secured ADMIN 관리자 전용
     * @param requestDto 강사 정보 양식
     * @return 강사 등록 후 responseDto 반환
     */
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/instructor")
    public InstructorResponseDto createInstructor(
            @RequestBody InstructorRequestDto requestDto) {
        return instructorService.createInstructor(requestDto);
    }

    /**
     * 강의 등록 (post)
     * @Secured ADMIN 관리자 전용
     * @param requestDto 강의 정보 양식
     * @return 강의 등록 후 responseDto 반환
     */
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/lecture")
    public LectureResponseDto createLecture(
            @RequestBody LectureRequestDto requestDto) {
        return lectureService.createLecture(requestDto);
    }

    /**
     * 선택한 강의 조회 기능 (get)
     * - 강의를 촬영한 강사의 정보를 확인할 수 있습니다.
     *     - 강사의 정보에 `전화번호`는 제외 되어있습니다.
     * 해당 강의 PK로 repository 에서 해당 강의 객체 조회
     * @param lectureId 강의 pk
     * @return 강의 조회후 호출된 객체 ResponseDto 반환
     */
    @GetMapping("/lecture/{lectureId}")
    public LectureFindResponseDto findLecture (
            @PathVariable Long lectureId) {
        return lectureService.findLecture(lectureId);
    }

    /**
     * 카테고리별 강의 목록 조회 기능 (get)
     * - 조회된 강의 목록은 선택한 기준에 의해 정렬됩니다.
     *     - `강의명`, `가격`, `등록일` 중 기준을 선택할 수 있습니다.
     *     - 내림차순, 오름차순을 선택할 수 있습니다.
     * 해당 카테고리로 repository 에서 해당 강의 List 조회
     * @param category (SPRING || REACT || NODE) '대문자'로 입력해야 Postman 테스트 가능 (Enum Type)
     * @return 강의 List 조회 후 ResponseDto List 반환
     */
    @GetMapping("/lecture/category/{category}/{selection}/{orderby}")
    public List<LectureResponseDto> getLecturesByCategory(
            @PathVariable LectureCategoryEnum category,
            @PathVariable String selection,
            @PathVariable String orderby) {
        return lectureService.getLecturesByCategory(category, selection, orderby);
    }

    /**
     * 선택한 강의 댓글 달기 기능 (post)
     * @Secured USER 회원만 댓글 등록이 가능합니다
     * @param lectureId 선택한 강의 lecture pk
     * @param commentRequestDto 댓글 내용 request
     * @param userDetails jwt 내 로그인 정보
     * @return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
     */
    @Secured(UserRoleEnum.Authority.USER)
    @PostMapping("/lecture/{lectureId}/comments")
    public ResponseEntity<CommentResponseDto> addCommentToLecture(
            @PathVariable Long lectureId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // userDetails를 사용하여 JWT에서 사용자 정보를 얻어올 수 있음
        CommentResponseDto responseDto = lectureService.addCommentToLecture(lectureId, commentRequestDto, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 선택한 강의의 선택한 댓글 수정 기능 (put)
     * @param lectureId 선택한 강의 lecture pk
     * @param commentID 선택한 댓글 comment pk
     * @param requestDto 댓글 수정할 내용 request
     * @param userDetails jwt 내 로그인 정보
     * @return ResponseEntity.status(HttpStatus.OK).body(responseDto)
     */
    @Secured(UserRoleEnum.Authority.USER)
    @PutMapping("/lecture/{lectureId}/comments/{commentID}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long lectureId, @PathVariable Long commentID,
            @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = lectureService.updateComment(lectureId, commentID, requestDto, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 선택한 강의의 선택한 댓글 삭제 기능 (delete)
     * @param lectureId 선택한 강의 lecture pk
     * @param commentID 선택한 댓글 comment pk
     * @param userDetails jwt 내 로그인 정보
     * @return ResponseEntity.ok("댓글을 성공적으로 삭제하였습니다.")
     */
    @Secured(UserRoleEnum.Authority.USER)
    @DeleteMapping("/lecture/{lectureId}/comments/{commentID}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long lectureId, @PathVariable Long commentID,
            @AuthenticationPrincipal UserDetails userDetails) {

        lectureService.deleteComment(lectureId, commentID, userDetails.getUsername());

        return ResponseEntity.ok("댓글을 성공적으로 삭제하였습니다.");
    }

    /**
     * 선택한 강의 좋아요 기능 (post)
     * - 좋아요 안되어있으면 좋아요
     * - 이미 좋아요 되어있으면 좋아요 취소
     * @param lectureId 선택한 강의 lecture pk
     * @param userDetails jwt 내 로그인 정보
     * @return 좋아요 or 좋아요 취소
     */
    @Secured(UserRoleEnum.Authority.USER)
    @PostMapping("/lecture/{lectureId}/likes")
    public ResponseEntity<String> likeOrUnLikeLecture(
            @PathVariable long lectureId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean liked = lectureService.likeOrUnLikeLecture(lectureId, userDetails.getUsername());

        if (!liked) {
            return ResponseEntity.ok("좋아요를 취소합니다.");
        }
        return ResponseEntity.ok("좋아요!");
    }
}
