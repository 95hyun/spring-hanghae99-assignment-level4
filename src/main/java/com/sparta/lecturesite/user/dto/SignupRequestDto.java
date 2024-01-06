package com.sparta.lecturesite.user.dto;

import com.sparta.lecturesite.user.entity.UserGender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 최소 8자에서 15자 사이로 포함해야 합니다.")
    private String password;

    @NotNull
    private UserGender gender;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    private boolean admin; // admin 할거면 true로

//    private String adminToken = ""; 관리자용 서버라서 테스트를 위해 일단 주석처리
}
