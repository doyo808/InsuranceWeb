$(function () {

    /** -----------------------------
     *  AJAX 함수: 서버에서 약관 로딩
     * ----------------------------- */
    function loadTerms(date) {
        // 로딩 인디케이터 표시
        $("#termsContent").html("<p>로딩 중...</p>");

        $.ajax({
            url: "/kdInsuranceTeam/terms/content/" + date, // contextpath 추가
            type: "GET",
            success: function(data) {
                $("#termsContent").html(data); // 성공 시 약관 내용 표시
            },
            error: function() {
                // 에러 시 사용자 친화적인 메시지 출력
                $("#termsContent").html("<p>약관을 불러올 수 없습니다. 다시 시도해 주세요.</p>");
            }
        });
    }

    /** -----------------------------
     *  1) 기본 <select> 기반 AJAX (커스텀 드롭다운 없는 페이지)
     * ----------------------------- */
    const select = $("#terms-effective-date");
    if (select.length) {
        // 드롭다운 변경 시
        select.on("change", function() {
            loadTerms($(this).val()); // 선택한 날짜로 AJAX 호출
        });

        // 페이지 최초 로딩 시 최신 날짜 자동 로드
        const latestDate = select.find("option:first").val();
        if (latestDate) {
            select.val(latestDate); // 최신 날짜 선택
            loadTerms(latestDate);  // 해당 날짜의 약관 로드
        }
    }

    /** -----------------------------
     *  2) 커스텀 드롭다운 + AJAX (커스텀 드롭다운 있는 페이지)
     * ----------------------------- */
    const customSelect = $('#customSelect');
    if (customSelect.length) {
        const selectedValue = customSelect.find('.selected-value'); // 선택된 값 표시
        const optionsList = customSelect.find('.options-list'); // 옵션 목록
        const options = optionsList.find('li'); // 옵션들

        // 드롭다운 열기
        function openDropdown() {
            customSelect.addClass('open');
            optionsList.removeClass('hidden');
        }

        // 드롭다운 닫기
        function closeDropdown() {
            customSelect.removeClass('open');
            optionsList.addClass('hidden');
        }

        // 클릭 시 열기/닫기
        customSelect.on('click', function(e) {
            e.stopPropagation();
            if (customSelect.hasClass('open')) {
                closeDropdown();
            } else {
                openDropdown();
            }
        });

        // 옵션 클릭 시 선택 + AJAX 호출
        options.on('click', function(e) {
            e.stopPropagation();
            options.removeClass('selected');
            $(this).addClass('selected'); // 선택한 옵션 표시

            // 선택된 날짜 값을 UI에 표시
            selectedValue.text($(this).text());

            // 선택한 날짜 값을 가져와서 AJAX 호출
            const selectedDate = $(this).data('value');
            loadTerms(selectedDate); // 해당 날짜의 약관 로드

            closeDropdown(); // 드롭다운 닫기
        });

        // 외부 클릭 시 드롭다운 닫기
        $(document).on('click', function() {
            closeDropdown();
        });

        // 키보드 접근성 (Enter, Space)
        customSelect.on('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                if (customSelect.hasClass('open')) {
                    closeDropdown();
                } else {
                    openDropdown();
                }
            }
        });

        // 페이지 최초 로딩 시 첫 번째 옵션으로 약관 표시
        const initialOption = options.first();
        initialOption.addClass('selected');
        selectedValue.text(initialOption.text()); // 첫 번째 옵션의 텍스트로 초기화
        const initialDate = initialOption.data('value'); // 첫 번째 옵션의 날짜 데이터
        loadTerms(initialDate); // 해당 날짜의 약관 로드
    }

});
