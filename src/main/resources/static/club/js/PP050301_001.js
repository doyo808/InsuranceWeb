const tabs = document.querySelectorAll(".content-main");
const buttons = document.querySelectorAll(".tab-btn");

// 2) URL의 탭 키 ↔ 내부 인덱스 매핑 표
//   - 주소창 ?tab=interest-free 처럼 들어오면 0번 탭을 보여준다.
const TAB_INDEX = {
	"interest-free": 0,
	"points": 1,
	"post-charge": 2
};
//   - 반대로, 현재 0/1/2번 탭을 주소창에 반영할 때 쓸 키 목록
const INDEX_TAB = ["interest-free", "points", "post-charge"];

// 3) 실제로 "i번째 탭을 보여줘"를 수행하는 함수
//    두 번째 인자는 옵션 객체이고, { updateUrl=true }가 기본값이다.
function showTab(index, { updateUrl = true } = {}) {
	// 3-1) 모든 탭을 돌면서 index와 같으면 보이게, 다르면 숨기게
	tabs.forEach((tab, i) => tab.classList.toggle("hidden", i !== index));
	// 3-2) 버튼도 같은 방식으로 'active' 스타일 토글
	buttons.forEach((btn, i) => btn.classList.toggle("active", i === index));

	// 3-3) 주소 표시줄 동기화: 사용자가 탭을 클릭하면 URL에 ?tab=... 를 반영
	if (updateUrl) {
		const url = new URL(window.location.href);             // 현재 주소를 URL 객체로 파싱
		url.searchParams.set('tab', INDEX_TAB[index]);         // ?tab=키 값을 현재 탭 키로 설정/교체
		history.pushState({ index }, '', url);                   // 브라우저 히스토리에 상태 푸시(뒤로가기 지원)
	}
}

// 4) 각 탭 버튼에 "클릭하면 그 순번 탭 보여주기" 이벤트 등록
buttons.forEach((btn, i) => {
	btn.addEventListener("click", () => showTab(i));         // 클릭 시 showTab(i). URL도 동기화됨
});

// 5) 뒤로가기/앞으로가기(브라우저 내비게이션) 할 때 실행될 핸들러
//    - pushState로 넣어둔 state를 읽어 같은 탭으로 복원한다.
window.addEventListener('popstate', (e) => {
	if (e.state && typeof e.state.index === 'number') {
		showTab(e.state.index, { updateUrl: false });             // 히스토리 이동 시엔 URL 다시 밀지 않음
	} else {
		// 어떤 이유로 state가 없으면 현재 URL을 다시 파싱해서 탭을 초기화
		initFromUrl({ updateUrl: false });
	}
});

// 6) "현재 URL"을 읽어 초기에 어떤 탭을 보여줄지 결정하는 함수
function initFromUrl({ updateUrl = false } = {}) {
	const params = new URLSearchParams(window.location.search); // ?뒤의 쿼리 문자열 파싱 객체
	const key = params.get('tab') || 'interest-free';           // ?tab=값이 없으면 기본 'interest-free'
	const index = TAB_INDEX[key] ?? 0;                          // 매핑표에 없으면 0번(첫 탭)
	showTab(index, { updateUrl });                                // 초기 표시(대개 URL 갱신은 안 함)
}

// 7) 문서가 처음 로드되면, URL 기준으로 초기 탭을 보여준다.
document.addEventListener('DOMContentLoaded', () => {
	initFromUrl();
});


// ----------------- modal --------------------
const modal = document.getElementById('termsModal');
const titleEl = document.getElementById('termsTitle');
const imgEl = document.getElementById('termsImg');
const contEl = document.getElementById('termsContent');
const ctaEl = document.getElementById('termsCta');
const termsClose = document.getElementById('termsX');
const termsOk = document.getElementById('termsOk');


document.querySelectorAll('.terms-trigger').forEach(btn => {
	btn.addEventListener('click', async () => {
		// 제목
		titleEl.textContent = btn.dataset.title || '사용 안내';

		// 이미지: 먼저 초기화
		imgEl.hidden = true;
		imgEl.removeAttribute('src');
		if (btn.dataset.img) {
			imgEl.src = btn.dataset.img;
			imgEl.hidden = false;
		}

		// 본문
		contEl.textContent = '';        // 초기화
		if (btn.dataset.url) {
			try {
				const res = await fetch(btn.dataset.url, { cache: 'no-store' });
				if (!res.ok) throw new Error('로드 실패');
				contEl.innerHTML = await res.text();
			} catch {
				contEl.textContent = '약관을 불러오는 중 오류가 발생했습니다.';
			}
		}

		// CTA: 항상 먼저 초기화
		ctaEl.hidden = true;
		ctaEl.style.display = 'none';     // ← 강제 비표시
		ctaEl.classList.add('is-hidden'); // ← 보호용 클래스 (위 CSS와 매칭)
		ctaEl.textContent = '';
		ctaEl.removeAttribute('href');
		ctaEl.setAttribute('aria-hidden', 'true');

		// 둘 다 있을 때만 노출
		const text = btn.dataset.ctaText?.trim();
		const href = btn.dataset.ctaHref?.trim();
		if (text && href) {
			ctaEl.textContent = text;
			ctaEl.href = href;
			ctaEl.hidden = false;
			ctaEl.style.display = '';       // ← 원래대로
			ctaEl.classList.remove('is-hidden');
			ctaEl.removeAttribute('aria-hidden');
		}

		modal.showModal();
	});
});

// 닫기
termsOk.onclick = termsClose.onclick = () => modal.close();



