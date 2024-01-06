package com.sparta.lecturesite.user.dto;

import com.sparta.lecturesite.user.entity.User;
import com.sparta.lecturesite.user.entity.UserGender;
import com.sparta.lecturesite.user.entity.UserRoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private Long id;
    private String email;
    private String password;
    private UserGender gender;
    private String phoneNumber;
    private String address;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role; // 권한 admin or user

    public SignupResponseDto(User saveUser) {
        this.id = saveUser.getId();
        this.email = saveUser.getEmail();
        this.password = saveUser.getPassword();
        this.gender = saveUser.getGender();
        this.phoneNumber = saveUser.getPhoneNumber();
        this.address = saveUser.getAddress();
        this.role = saveUser.getRole();
    }
}
