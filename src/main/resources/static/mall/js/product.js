/* .notice 클래스 열고 닫기 */

$('.notice').click(function (e) {
    // 자바스크립트로 DOM 객체를 불러오고
    // 제이커리의 선택자?로 객체를 넣은후 find메서드를 통해 객체의 요소를 선택한다
    // 선택된 요소로 제이커리의 전용 메서드를 사용할 수 있다.
    $(this).find("dd").slideToggle();
});

/* 탭 전환 기능 */
$('.main-section > .tabs > .tab-menu').click(function (e) {
    // 겉에 있는 탭
    if (!$(this).hasClass('selected')) {
        $('.main-section > .tabs > .tab-menu').removeClass('selected');
        $(this).addClass('selected');

        $('.main-section > .tab').removeClass('show');
        $('#' + $(this).data('tab')).addClass('show');
    }

});

$('.tab > .tabs > .tab-menu').click(function (e) {
    // 탭안의 탭
    if (!$(this).hasClass('selected')) {
        $('.tab > .tabs > .tab-menu').removeClass('selected');
        $(this).addClass('selected');

        $('.tab > .inner-tab').removeClass('show');
        $('#' + $(this).data('tab')).addClass('show');
    }
});