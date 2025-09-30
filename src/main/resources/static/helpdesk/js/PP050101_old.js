// 파일: /js/custom-dropdown.js

$(document).ready(function () {
  $('.dropdown').each(function () {
    const $dropdown = $(this);

    // 드롭다운 버튼 클릭
    $dropdown.find('.dropdown-btn').on('click', function (e) {
      e.stopPropagation(); // 다른 이벤트 차단
      const $menu = $dropdown.find('.dropdown-menu');

      // 열려있는 다른 드롭다운 닫기
      $('.dropdown-menu').not($menu).hide();

      // 현재 메뉴 토글
      $menu.toggle();
    });

    // 드롭다운 항목 클릭
    $dropdown.find('.dropdown-menu li').on('click', function () {
      const selectedText = $(this).text();
      $dropdown.find('.dropdown-btn').html(`${selectedText} <span class="arrow">▼</span>`);
      $dropdown.find('.dropdown-menu').hide();
    });
  });

  // 바깥 클릭 시 드롭다운 닫기
  $(document).on('click', function () {
    $('.dropdown-menu').hide();
  });
});


document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll('.accordion-header').forEach(header => {
    header.addEventListener('click', () => {
      const item = header.parentElement;
      item.classList.toggle('active');
    });
  });
});



