document.addEventListener("DOMContentLoaded", function() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.getAttribute('data-tab');

            // 버튼 활성화 처리
            tabButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            // 탭 내용 표시 처리
            tabContents.forEach(content => {
                if(content.id === target) {
                    content.style.display = 'block';
                } else {
                    content.style.display = 'none';
                }
            });
        });
    });
});
