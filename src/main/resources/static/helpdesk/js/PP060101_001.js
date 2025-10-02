document.addEventListener('DOMContentLoaded', function () {
    const keywordInput = document.getElementById('keyword');
    const searchBtn = document.getElementById('searchBtn');
    const categories = document.querySelectorAll('.faq-categories span');
    const faqList = document.getElementById('faqList');

    let currentCategory = "전체";

    // AJAX로 FAQ 로드
    function loadFaqs(category, keyword, push = true) {
        fetch(contextPath + 'helpdesk/PP060101_001/search', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `category=${encodeURIComponent(category)}&keyword=${encodeURIComponent(keyword)}`
        })
        .then(res => res.text())
        .then(html => {
            faqList.innerHTML = html;
            bindFaqToggle();
        })
        .catch(err => console.error(err));
    }

    // FAQ 클릭 시 토글
    function bindFaqToggle() {
        const questions = document.querySelectorAll('.faq-question');
        questions.forEach(q => {
            q.addEventListener('click', () => {
                questions.forEach(other => {
                    if (other !== q) other.classList.remove('active');
                });
                q.classList.toggle('active');
            });
        });
    }

    // 검색 버튼 클릭
    if (searchBtn && keywordInput) {
        searchBtn.addEventListener('click', function () {
            currentCategory = document.querySelector('.faq-categories span.active')?.dataset.category || '전체';
            loadFaqs(currentCategory, keywordInput.value);
        });
    }

    // 엔터키 입력 시 검색
    keywordInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault(); // form submit 막기
            searchBtn.click();
        }
    });

    // 카테고리 클릭
    categories.forEach(cat => {
        cat.addEventListener('click', function () {
            categories.forEach(c => c.classList.remove('active'));
            this.classList.add('active');

            currentCategory = this.dataset.category || '전체';
            loadFaqs(currentCategory, keywordInput.value);
        });
    });

    // 초기 FAQ 토글 바인딩
    bindFaqToggle();
});
