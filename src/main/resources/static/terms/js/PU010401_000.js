$(function () {

    /** -----------------------------
     *  AJAX 함수: 서버에서 약관 로딩
     * ----------------------------- */
    function loadTerms(date) {
        $.ajax({
            url: "/terms/content/" + encodeURIComponent(date), // 안전하게 URL Encoding
            type: "GET",
            success: function(data) {
                $("#termsContent").html(data);
            },
            error: function(xhr) {
                $("#termsContent").html("<p>약관을 불러올 수 없습니다. (오류 코드: " + xhr.status + ")</p>");
            }
        });
    }

    /** -----------------------------
     *  기본 <select> + 커스텀 드롭다운 처리
     * ----------------------------- */
    function initSelect(selectElem, isCustom) {
        if (!selectElem.length) return;

        if (isCustom) {
            const selectedValue = selectElem.find('.selected-value');
            const optionsList = selectElem.find('.options-list');
            const options = optionsList.find('li');

            function openDropdown() {
                selectElem.addClass('open');
                optionsList.removeClass('hidden');
            }

            function closeDropdown() {
                selectElem.removeClass('open');
                optionsList.addClass('hidden');
            }

            selectElem.on('click', function(e) {
                e.stopPropagation();
                selectElem.hasClass('open') ? closeDropdown() : openDropdown();
            });

            options.on('click', function(e) {
                e.stopPropagation();
                options.removeClass('selected');
                $(this).addClass('selected');

                selectedValue.text($(this).text());
                const selectedDate = $(this).data('value');
                loadTerms(selectedDate);
                closeDropdown();
            });

            $(document).on('click', closeDropdown);

            selectElem.on('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    selectElem.hasClass('open') ? closeDropdown() : openDropdown();
                }
            });

            const initialOption = options.first();
            initialOption.addClass('selected');
            selectedValue.text(initialOption.text());
            loadTerms(initialOption.data('value'));

        } else {
            selectElem.on("change", function() {
                loadTerms($(this).val());
            });

            const latestDate = selectElem.find("option:first").val();
            if (latestDate) {
                selectElem.val(latestDate);
                loadTerms(latestDate);
            }
        }
    }

    // 초기화
    initSelect($("#terms-effective-date"), false); // 기본 select
    initSelect($("#customSelect"), true);          // 커스텀 select
});
