// jQuery가 로드된 후 실행
$(function () {
    /** -----------------------------
     *  약관 AJAX 로딩 (jQuery)
     * ----------------------------- */
    function loadTerms(date) {
        $.ajax({
            url: "/terms/content/" + date,
            type: "GET",
            success: function (data) {
                $("#termsContent").html(data);
            },
            error: function () {
                $("#termsContent").html("<p>약관을 불러올 수 없습니다.</p>");
            }
        });
    }

    // 드롭다운 변경 시 약관 로드
    $("#terms-effective-date").on("change", function () {
        loadTerms($(this).val());
    });

    // 최초 로딩 시 select의 첫 번째(최신) 날짜 자동 로드
    let latestDate = $("#terms-effective-date option:first").val();
    if (latestDate) {
        $("#terms-effective-date").val(latestDate);
        loadTerms(latestDate);
    }
});
