document.addEventListener('DOMContentLoaded', function() {
    const questions = document.querySelectorAll('.faq-question');

    questions.forEach(q => {
        q.addEventListener('click', () => {
            // 이미 열린 다른 질문 닫기
            questions.forEach(other => {
                if (other !== q) {
                    other.classList.remove('active');
                }
            });
            // 클릭한 질문 토글
            q.classList.toggle('active');
        });
    });
});