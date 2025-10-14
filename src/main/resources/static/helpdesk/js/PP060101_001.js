document.addEventListener('DOMContentLoaded', function () {
    const faqList = document.getElementById('faqList');
    const keywordInput = document.getElementById('keyword');
    const searchBtn = document.getElementById('searchBtn');
    const categories = document.querySelectorAll('.faq-categories span');

    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    let currentCategory = "전체";
    let currentPage = 1;

    function bindFaqToggle() {
        const questions = faqList.querySelectorAll('.faq-question');
        questions.forEach(q => {
            q.addEventListener('click', () => {
                questions.forEach(other => {
                    if (other !== q) other.classList.remove('active');
                });
                q.classList.toggle('active');
            });
        });
    }

    function bindPagination() {
        const pageLinks = faqList.querySelectorAll('.page-link');
        pageLinks.forEach(link => {
            const page = parseInt(link.dataset.page);
            link.addEventListener('click', () => {
                loadFaqs(currentCategory, keywordInput.value, page);
            });
        });
    }

    function loadFaqs(category, keyword, page = 1) {
        currentCategory = category;
        currentPage = page;

        fetch(`${contextPath}helpdesk/PP060101_001/search`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            },
            body: `category=${encodeURIComponent(category)}&keyword=${encodeURIComponent(keyword)}&page=${page}`
        })
        .then(res => res.text())
        .then(html => {
            faqList.innerHTML = html;
            bindFaqToggle();
            bindPagination();
        })
        .catch(err => console.error(err));
    }

    // 검색
    searchBtn.addEventListener('click', () => loadFaqs(currentCategory, keywordInput.value, 1));
    keywordInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            searchBtn.click();
        }
    });

    // 카테고리 클릭
    categories.forEach(cat => {
        cat.addEventListener('click', function () {
            categories.forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            loadFaqs(this.dataset.category, keywordInput.value, 1);
        });
    });

    // 초기 바인딩
    bindFaqToggle();
    bindPagination();
});
