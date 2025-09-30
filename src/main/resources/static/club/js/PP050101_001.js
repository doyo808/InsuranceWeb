$(document).ready(function () {

    // --- 커스텀 드롭다운 ---
    $('.dropdown').each(function () {
        const $dropdown = $(this);

        $dropdown.find('.dropdown-btn').on('click', function (e) {
            e.stopPropagation();
            const $menu = $dropdown.find('.dropdown-menu');
            $('.dropdown-menu').not($menu).hide();
            $menu.toggle();
        });

        $dropdown.find('.dropdown-menu li').on('click', function () {
            const selectedText = $(this).text().trim();
            $dropdown.find('.dropdown-btn').html(selectedText + ' <span class="arrow">▼</span>');
            $dropdown.find('.dropdown-menu').hide();
        });
    });

    // 바깥 클릭 시 모든 드롭다운 닫기
    $(document).on('click', function () {
        $('.dropdown-menu').hide();
    });

    // --- 아코디언 ---
    $('.accordion-header').on('click', function () {
        const $item = $(this).closest('.accordion-item');
        $('.accordion-item').not($item).removeClass('active');
        $item.toggleClass('active');
    });

    // --- 검색 버튼 클릭 ---
    $('.search-btn').on('click', function () {
        const st = $('.search-box .dropdown-btn').text().trim();
        const searchType = st === '내용' ? 'content' : st === '작성자' ? 'authorName' : '';
        const searchKeyword = $('.search-input').val().trim();
        const subCategory = $('.dropdown.right .dropdown-btn').text().trim();
        const category = $('.tabs a.active span').text().trim();

        const url = new URL(window.location.origin + '/club/PP050101_001');
        url.searchParams.set('category', category);
        if (subCategory && subCategory !== '전체') {
            url.searchParams.set('subCategory', subCategory);
        }
        if (searchKeyword) {
            url.searchParams.set('searchType', searchType);
            url.searchParams.set('searchKeyword', searchKeyword);
        }
        url.searchParams.set('page', 1);
        window.location.href = url.toString();
    });

    // --- 탭 메뉴 클릭 ---
    $('.tabs a').on('click', function (e) {
        e.preventDefault();
        const category = $(this).text().trim();
        const st = $('.search-box .dropdown-btn').text().trim();
        const searchType = st === '내용' ? 'content' : st === '작성자' ? 'authorName' : '';
        const searchKeyword = $('.search-input').val().trim();
        const subCategory = $('.dropdown.right .dropdown-btn').text().trim();

        const url = new URL(window.location.origin + '/club/PP050101_001');
        url.searchParams.set('category', category);
        if (subCategory && subCategory !== '전체') {
            url.searchParams.set('subCategory', subCategory);
        }
        if (searchKeyword) {
            url.searchParams.set('searchType', searchType);
            url.searchParams.set('searchKeyword', searchKeyword);
        }
        url.searchParams.set('page', 1);
        window.location.href = url.toString();
    });

});
