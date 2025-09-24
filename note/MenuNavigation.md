====================================
	메뉴 네비게이션 링크 등록 가이드
====================================

1. 경로
   templates/common/fragments/ 하위 메뉴에 각 기능 HTML 파일 생성
------------------------------------

2. 클래스 설명
   - submenu_col
       각 메뉴바에서 가로로 분할된 영역
   - submenu_col_frag
       submenu_col 내부에서 기능 단위를 구분
   - submenu_col_title
       기능 단위의 제목, 볼드체로 표시
   - submenu_col_menu
       세부 기능 이름, 하이퍼링크 역할
-----------------------------------

3. HTML 구조 예시

<div class="submenu_col">
    <div class="submenu_col_frag">
        <div class="submenu_col_title">내 정보 확인/변경</div>
        <div class="submenu_col_menu">
            <a th:href="@{/mypage/MPDG0093}">내 정보 확인/변경</a>
        </div>
        <div class="submenu_col_menu">
            <a th:href="@{/mypage/MPDG0247}">마케팅 정보 활용 동의/철회</a>
        </div>
    </div>
</div>
-----------------------------------

4. 기능 추가 방법
   1) submenu_col_title
      - 기능 단위의 제목 작성
   2) submenu_col_menu
      - a 태그 안에 접속할 링크 작성
