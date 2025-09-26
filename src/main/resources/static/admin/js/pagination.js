
class AppPagination extends HTMLElement {
  static get observedAttributes() { return ["current","total","href","preserve-query"]; }

  connectedCallback() { this.render(); }
  attributeChangedCallback() { this.render(); }

  get current() { return parseInt(this.getAttribute("current") || "1", 10); }
  get total()   { return parseInt(this.getAttribute("total")   || "1", 10); }

  // href 패턴: 예) "/admin/contract/receipts?page={page}"
  buildHref(page) {
    const pattern = this.getAttribute("href") || "#";
    let url = pattern.replace("{page}", page);
    if (this.hasAttribute("preserve-query")) {
      const qs = window.location.search;
      if (qs) {
        const u = new URL(url, window.location.origin);
        const cur = new URLSearchParams(qs);
        // 기존 url에 이미 있는 파라미터는 보존
        cur.forEach((v, k) => { if (!u.searchParams.has(k)) u.searchParams.set(k, v); });
        url = u.pathname + (u.search ? u.search : "");
      }
    }
    return url;
  }

  render() {
    if (!this.isConnected) return;
    const cur = Math.max(1, Math.min(this.current, this.total));
    const prevDisabled = cur <= 1;
    const nextDisabled = cur >= this.total;

    // 간단한 범위 (필요하면 윈도우링 로직으로 확장)
    const pages = Array.from({length: this.total}, (_, i) => i + 1);

    this.innerHTML = `
      <nav class="pagination" aria-label="페이지네이션">
        <a class="pagination__btn${prevDisabled ? " is-disabled" : ""}" 
           href="${prevDisabled ? "#" : this.buildHref(cur - 1)}">이전</a>
        <ul class="pagination__list">
          ${pages.map(p => `
            <li>
              <a class="pagination__page${p === cur ? " is-active" : ""}" 
                 href="${this.buildHref(p)}">${p}</a>
            </li>
          `).join("")}
        </ul>
        <a class="pagination__btn${nextDisabled ? " is-disabled" : ""}" 
           href="${nextDisabled ? "#" : this.buildHref(cur + 1)}">다음</a>
      </nav>
    `;
  }
}
customElements.define("app-pagination", AppPagination);

