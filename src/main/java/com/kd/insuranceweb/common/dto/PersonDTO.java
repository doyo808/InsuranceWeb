package com.kd.insuranceweb.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTO {

    // ======================
    // 정규식 상수 정의
    // ======================
    private static final String NAME_PATTERN = "^[가-힣]{2,}$";
    private static final String PERSONAL_ID_PATTERN = "^\\d{6}-[1-4]\\d{6}$";

    // ======================
    // 필드 정의
    // ======================

    // ----------------------
    // 1. PK
    // ----------------------
    private Integer person_id;

    // ----------------------
    // 2. 이름
    // ----------------------
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(
        regexp = NAME_PATTERN,
        message = "이름은 최소 2글자 이상의 완성형 한글이어야 합니다."
    )
    private String person_name;

    // ----------------------
    // 3. 이메일
    // ----------------------
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    // ----------------------
    // 4. 주민등록번호
    // ----------------------
    @NotBlank(message = "주민등록번호는 필수 입력 값입니다.")
    @Pattern(
        regexp = PERSONAL_ID_PATTERN,
        message = "주민등록번호 형식이 올바르지 않습니다. (앞6자리-뒤7자리, 뒤 첫자리 1~4)"
    )
    private String personal_id;

    // ----------------------
    // 5. 전화번호
    // ----------------------
    private String phone_number;
}
