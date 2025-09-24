==================================================== 
			공통 레이아웃 적용하기
====================================================

[1] 적용 방법
1. <html> 태그 변경
• HTML 파일 최상단의 <html> 태그를 아래와 같이 수정합니다.

<html lang="ko" xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{/common/layout.html}">
----------------------------------------------------

[2] <main> 태그 추가
• <body> 태그 바로 안쪽에 <main layout:fragment="content"> 태그를 추가하고, </body> 태그가 닫히기 직전에 </main>으로 닫아줍니다.

<body>
    <main layout:fragment="content">
    </main>
</body>
----------------------------------------------------

※ 참고사항
- 위와 같이 적용하면 작성한 HTML이 공통 레이아웃의 content 영역으로 지정됩니다.
- content 영역에는 자동으로 width: 1200px 스타일이 적용됩니다.