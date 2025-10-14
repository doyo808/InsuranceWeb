document.addEventListener("DOMContentLoaded", function() {
    const tabs = document.querySelectorAll(".tab-menu a");
    const panes = document.querySelectorAll(".tab-pane");

    tabs.forEach(tab => {
        tab.addEventListener("click", function(e) {
            e.preventDefault();
            const menu = this.dataset.tab;

            // 탭 active 처리
            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            // 콘텐츠 show/hide 처리
            panes.forEach(p => p.classList.toggle("active", p.id === menu));

            // URL 반영 (뒤로가기 지원)
            history.pushState({}, "", `?menu=${menu}`);
        });
    });

    // 뒤로가기/앞으로가기 처리
    window.addEventListener("popstate", () => {
        const params = new URLSearchParams(location.search);
        const menu = params.get("menu") || "ars1";

        tabs.forEach(t => t.classList.toggle("active", t.dataset.tab === menu));
        panes.forEach(p => p.classList.toggle("active", p.id === menu));
    });
});
