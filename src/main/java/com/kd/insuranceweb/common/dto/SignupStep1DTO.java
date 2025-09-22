package com.kd.insuranceweb.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupStep1DTO {

    // ----------------------
    // 이름: 최소 2글자 이상 완성형 한글
    // ----------------------
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[가-힣]{2,}$", message = "이름은 최소 2글자 이상의 완성형 한글이어야 합니다.")
    private String korName;

    // ----------------------
    // 주민등록번호 앞 6자리: 숫자만
    // ----------------------
    @NotBlank(message = "주민등록번호 앞 6자리는 필수 입력 값입니다.")
    @Pattern(regexp = "\\d{6}", message = "주민등록번호 앞 6자리는 숫자 6자리여야 합니다.")
    private String jumin6;

    // ----------------------
    // 주민등록번호 뒤 7자리: 1~4로 시작, 숫자 7자리
    // ----------------------
    @NotBlank(message = "주민등록번호 뒤 7자리는 필수 입력 값입니다.")
    @Pattern(regexp = "^[1-4]\\d{6}$", message = "주민등록번호 뒤 7자리는 1~4로 시작하는 숫자 7자리여야 합니다.")
    private String jumin7;

    // ----------------------
    // 전화번호: 한국 휴대폰 / 일반전화
    // ----------------------

    private String phoneNumber;

    // ----------------------
    // 이메일
    // ----------------------
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String eMail;
}
