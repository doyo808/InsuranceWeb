$(function () {

    /** -----------------------------
     *  AJAX 함수: 서버에서 약관 로딩
     * ----------------------------- */
    function loadTerms(date) {
        $.ajax({
            url: "/terms/content/" + date,
            type: "GET",
            success: function(data) {
                $("#termsContent").html(data);
            },
            error: function() {
                $("#termsContent").html("<p>약관을 불러올 수 없습니다.</p>");
            }
        });
    }

    /** -----------------------------
     *  커스텀 드롭다운 이벤트
     * ----------------------------- */
    const customSelect = $('#customSelect');
    const selectedValue = customSelect.find('.selected-value');
    const optionsList = customSelect.find('.options-list');
    const options = optionsList.find('li');

    function openDropdown() {
        customSelect.addClass('open');
        optionsList.removeClass('hidden');
    }

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

    // 옵션 클릭 시 선택 + AJAX
    options.on('click', function(e) {
        e.stopPropagation();
        options.removeClass('selected');
        $(this).addClass('selected');

        // 선택값 UI 업데이트
        selectedValue.text($(this).text());

        // 선택한 날짜 값 가져오기
        const selectedDate = $(this).data('value');

        // AJAX 호출
        loadTerms(selectedDate);

        closeDropdown();
    });

    // 외부 클릭 시 닫기
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

    /** -----------------------------
     *  페이지 최초 로딩 시 최신 날짜 약관 표시
     * ----------------------------- */
    const initialOption = options.first();
    initialOption.addClass('selected');
    selectedValue.text(initialOption.text());
    const initialDate = initialOption.data('value');
    loadTerms(initialDate);
});
