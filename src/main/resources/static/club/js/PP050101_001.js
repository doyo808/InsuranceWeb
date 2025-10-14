$(document).ready(function () {

    // ================================
    // 1. 드롭다운 처리
    // ================================
    $('.dropdown').each(function () {
        const $dropdown = $(this);

        // 드롭다운 버튼 클릭
        $dropdown.find('.dropdown-btn').on('click', function (e) {
            e.stopPropagation();
            const $menu = $dropdown.find('.dropdown-menu');
            $('.dropdown-menu').not($menu).hide(); // 다른 메뉴 닫기
            $menu.toggle();
        });

        // 드롭다운 항목 클릭
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

    // ================================
    // 2. 아코디언 처리
    // ================================
    $('.accordion-header').on('click', function () {
        const $item = $(this).closest('.accordion-item');
        $('.accordion-item').not($item).removeClass('active'); // 다른 항목 닫기
        $item.toggleClass('active');
    });

    // ================================
    // 3. 검색 버튼 클릭
    // ================================
    $('.search-btn').on('click', function () {
        const st = $('.search-box .dropdown-btn').first().text().trim();
        const searchType = st === '내용' ? 'content' : st === '작성자' ? 'author_name' : '';
        const searchKeyword = $('.search-input').val().trim();
        const subCategory = $('.dropdown.right .dropdown-btn').text().trim();
        const category = $('.tabs a.active span').text().trim();

        const url = new URL(window.location.origin + contextPath + 'club/PP050101_001');
        url.searchParams.set('category', category);
        if (subCategory && subCategory !== '전체') url.searchParams.set('subCategory', subCategory);
        if (searchKeyword) {
            url.searchParams.set('searchType', searchType);
            url.searchParams.set('searchKeyword', searchKeyword);
        }
        url.searchParams.set('page', 1);
        window.location.href = url.toString();
    });

    // ================================
    // 4. 탭 메뉴 클릭
    // ================================
    $('.tabs a').on('click', function (e) {
        e.preventDefault();

        // span 안쪽 글자를 정확히 가져오기
        const category = $(this).find('span').text().trim();
        const st = $('.search-box .dropdown-btn').first().text().trim();
        const searchType = st === '내용' ? 'content' : st === '작성자' ? 'author_name' : '';
        const searchKeyword = $('.search-input').val().trim();
        const subCategory = $('.dropdown.right .dropdown-btn').text().trim();

        const url = new URL(window.location.origin + contextPath + 'club/PP050101_001');
        url.searchParams.set('category', category);
        if (subCategory && subCategory !== '전체') url.searchParams.set('subCategory', subCategory);
        if (searchKeyword) {
            url.searchParams.set('searchType', searchType);
            url.searchParams.set('searchKeyword', searchKeyword);
        }
        url.searchParams.set('page', 1);
        window.location.href = url.toString();
    });

    // ================================
    // 5. 페이징 클릭 처리
    // ================================
    $('.pagination-list a').on('click', function (e) {
        e.preventDefault();
        const href = $(this).attr('href');
        if (href) window.location.href = href;
    });

});
