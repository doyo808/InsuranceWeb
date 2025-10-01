class AppPagination extends HTMLElement {
	static get observedAttributes() {
		return ["current", "total", "total-items", "per-page", "href", "preserve-query", "window"];
	}

	connectedCallback() { this.render(); }
	attributeChangedCallback() { this.render(); }

	// 현재/총 페이지 계산
	get current() { return Math.max(1, parseInt(this.getAttribute("current") || "1", 10)); }
	get perPage() { return Math.max(1, parseInt(this.getAttribute("per-page") || "10", 10)); }
	get totalItems() { return parseInt(this.getAttribute("total-items") || "0", 10); }
	get totalPagesFromItems() {
		return Math.max(1, Math.ceil(this.totalItems / this.perPage));
	}
	// 하위호환: total(=총 페이지수)도 허용
	get totalPages() {
		if (this.hasAttribute("total-items")) return this.totalPagesFromItems;
		const t = parseInt(this.getAttribute("total") || "1", 10);
		return Math.max(1, t);
	}
	get win() { return Math.max(1, parseInt(this.getAttribute("window") || "5", 10)); } // 표시 최대 번호 개수(기본 5)

	// href 패턴: "/admin/claim?page={page}"
	buildHref(page) {
		const pattern = this.getAttribute("href") || "#";
		let url = pattern.replace("{page}", page);
		if (this.hasAttribute("preserve-query")) {
			const qs = window.location.search;
			if (qs) {
				const u = new URL(url, window.location.origin);
				const cur = new URLSearchParams(qs);
				cur.forEach((v, k) => { if (!u.searchParams.has(k)) u.searchParams.set(k, v); });
				url = u.pathname + (u.search ? u.search : "");
			}
		}
		return url;
	}

	// 가시 페이지 윈도우 계산 (… 포함)
	calcWindow(cur, total, size) {
		let start = Math.max(1, cur - Math.floor(size / 2));
		let end = Math.min(total, start + size - 1);
		start = Math.max(1, end - size + 1);

		const pages = [];
		if (start > 1) pages.push(1, "left-ellipsis");   // 1 … 
		for (let p = start; p <= end; p++) pages.push(p); // window
		if (end < total) pages.push("right-ellipsis", total); // … last
		return pages;
	}

	render() {
		if (!this.isConnected) return;

		const total = this.totalPages;
		const cur = Math.min(this.current, total);
		const prevDisabled = cur <= 1;
		const nextDisabled = cur >= total;

		const blocks = this.calcWindow(cur, total, this.win).map(p => {
			if (p === "left-ellipsis" || p === "right-ellipsis") {
				return `<li class="pagination__ellipsis" aria-hidden="true">…</li>`;
			}
			const active = p === cur ? " is-active" : "";
			return `<li><a class="pagination__page${active}" href="${this.buildHref(p)}">${p}</a></li>`;
		}).join("");

		this.innerHTML = `
      <nav class="pagination" aria-label="페이지네이션">
        <a class="pagination__btn${prevDisabled ? " is-disabled" : ""}" 
           href="${prevDisabled ? "#" : this.buildHref(cur - 1)}">이전</a>
        <ul class="pagination__list">${blocks}</ul>
        <a class="pagination__btn${nextDisabled ? " is-disabled" : ""}" 
           href="${nextDisabled ? "#" : this.buildHref(cur + 1)}">다음</a>
      </nav>
    `;
	}
}
customElements.define("app-pagination", AppPagination);
