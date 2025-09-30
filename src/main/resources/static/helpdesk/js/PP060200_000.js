document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tab-menu a');
    tabs.forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();
            const menu = this.getAttribute('data-tab');

            // 모든 tab-pane 숨기기
            document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));

            // 클릭한 탭의 콘텐츠 표시
            document.getElementById(menu).classList.add('active');

            // 탭 메뉴 active 상태 변경
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
        });
    });
});
