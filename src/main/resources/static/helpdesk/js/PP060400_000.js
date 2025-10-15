$(document).ready(function () {
    let currentPage = 1;
    const PAGE_SIZE = 10;
    let totalCount = 0;

    // ===============================
    // 1. ContextPath 가져오기
    // ===============================
    let ctxPath = '';
    try {
        if (typeof contextPath !== 'undefined' && contextPath) {
            ctxPath = contextPath;
        }
    } catch (e) {}
    
    if (!ctxPath || ctxPath === '/') {
        const pathName = window.location.pathname;
        const pathParts = pathName.split('/').filter(p => p.length > 0);
        if (pathParts.length > 0) {
            ctxPath = '/' + pathParts[0] + '/';
        } else {
            ctxPath = '/';
        }
    }
    if (!ctxPath.endsWith('/')) ctxPath += '/';
    console.log('[DEBUG] ctxPath:', ctxPath);

    // ===============================
    // 2. CSRF 토큰 설정 (Spring Security)
    // ===============================
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    $.ajaxSetup({
        beforeSend: function(xhr) {
            if (csrfToken && csrfHeader) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }
    });

    // ===============================
    // 3. URL 파라미터 읽기
    // ===============================
    function getQueryParam(param) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }

    // ===============================
    // 4. 공지사항 목록 렌더링
    // ===============================
    function renderNotices(notices) {
        const container = $('#search-results');
        container.empty();

        if (!notices || notices.length === 0) {
            container.append('<div class="no-result">검색 결과가 없습니다.</div>');
            return;
        }

        notices.forEach(function (notice) {
            const formattedDate = notice.created_at || '-';
            const item = $(`
                <div class="notice-item" data-id="${notice.notice_id}">
                    <span class="notice-title">${notice.title}</span>
                    <span class="notice-date">${formattedDate}</span>
                </div>
            `);
            container.append(item);
        });

        $('#current-page').text(currentPage);
    }

    // ===============================
    // 5. 페이징 버튼 렌더링
    // ===============================
    function renderPagination() {
        const totalPages = Math.ceil(totalCount / PAGE_SIZE);
        let paginationHtml = '';

        paginationHtml += `<button id="first-page" ${currentPage === 1 ? 'disabled' : ''}>&lt;&lt;</button>`;
        paginationHtml += `<button id="prev-page" ${currentPage === 1 ? 'disabled' : ''}>&lt;</button>`;

        for (let i = 1; i <= totalPages; i++) {
            paginationHtml += `<button class="page-btn" data-page="${i}" ${i === currentPage ? 'disabled' : ''}>${i}</button>`;
        }

        paginationHtml += `<button id="next-page" ${currentPage === totalPages ? 'disabled' : ''}>&gt;</button>`;
        paginationHtml += `<button id="last-page" ${currentPage === totalPages ? 'disabled' : ''}>&gt;&gt;</button>`;

        $('.pagination').html(paginationHtml);
    }

    // ===============================
    // 6. 공지사항 로드 (AJAX)
    // ===============================
    function loadNotices(page = 1, keyword = '') {
        $.ajax({
            url: ctxPath + 'helpdesk/list',
            type: 'GET',
            data: { page: page, keyword: keyword },
            dataType: 'json',
            success: function (data) {
                renderNotices(data.notices);
                totalCount = data.totalCount;
                currentPage = data.currentPage;
                renderPagination();
            },
            error: function (xhr) {
                console.error('[공지사항 로드 실패]', xhr);
                alert('공지사항을 불러오는데 실패했습니다.');
            }
        });
    }

    // ===============================
    // 7. 검색 기능
    // ===============================
    $('#search-form').on('submit', function (e) {
        e.preventDefault();
        const keyword = $('#search-input').val().trim();
        currentPage = 1;
        loadNotices(currentPage, keyword);
    });

    // ===============================
    // 8. 상세페이지 이동
    // ===============================
    $('#search-results').on('click', '.notice-item', function () {
        const noticeId = $(this).data('id');
        const keyword = $('#search-input').val().trim();
        window.location.href =
            ctxPath + 'helpdesk/detail/' + noticeId +
            '?page=' + currentPage +
            '&keyword=' + encodeURIComponent(keyword);
    });

    // ===============================
    // 9. 페이지 버튼 클릭 처리
    // ===============================
    $('.pagination').on('click', 'button', function () {
        const btnId = $(this).attr('id');
        const keyword = $('#search-input').val().trim();
        const totalPages = Math.ceil(totalCount / PAGE_SIZE);

        if (btnId === 'first-page') loadNotices(1, keyword);
        else if (btnId === 'prev-page' && currentPage > 1) loadNotices(currentPage - 1, keyword);
        else if (btnId === 'next-page' && currentPage < totalPages) loadNotices(currentPage + 1, keyword);
        else if (btnId === 'last-page') loadNotices(totalPages, keyword);
        else if ($(this).hasClass('page-btn')) {
            const page = Number($(this).data('page'));
            if (page !== currentPage) loadNotices(page, keyword);
        }
    });

    // ===============================
    // 10. 초기 로딩 시 URL 파라미터 반영
    // ===============================
    const initialPage = parseInt(getQueryParam('page')) || 1;
    const initialKeyword = getQueryParam('keyword') || '';
    $('#search-input').val(initialKeyword);
    loadNotices(initialPage, initialKeyword);
});
