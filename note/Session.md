====================================================
                 로그인 처리 가이드
====================================================

[1] 로그인 처리 흐름
----------------------------------------------------
- 입력 값:
    • 로그인 ID
    • 로그인 PW

- 처리 위치:
    /common/service/CustomUserDetailsService.java

- 세션에 등록되는 값:
    • customer_id    : 고객 테이블 PK
    • person_id      : 사람 테이블 PK
                        (이름, 주민번호, 전화번호, 이메일 조회 시 필요)
    • login_id       : 사이트 로그인 ID
    • password_hash  : 사이트 로그인 PW 해시값


[2] 로그인 성공 후 처리
----------------------------------------------------
- 처리 위치:
    /common/advice/GlobalUserInfoAdvice.java

- 모델에 등록되는 이름:
    "loginUser"


[3] 컨트롤러에서 사용법
----------------------------------------------------
예시 코드:

public String openEditMyInfo(
        @AuthenticationPrincipal CustomUserDetails loginUser,
        Model model) {

    if (loginUser != null) {
        PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
        CustomerDTO customer = customerMapper.selectByLoginId(loginUser.getUsername());

        if (person != null && customer != null) {
            loginUser.setPerson(person);
            loginUser.setCustomer(customer);

            model.addAttribute("loginUser", loginUser);
        }
    }
    return "/mypage/EditMyInfo.html";
}

※ 주의사항
- loginUser는 반드시 NULL 체크 필요
- PersonDTO, CustomerDTO 조회 후 loginUser에 다시 세팅
- 최종적으로 model에 "loginUser"를 담아 화면에서 사용
