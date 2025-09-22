const tabs = document.querySelectorAll(".content-main");
const buttons = document.querySelectorAll(".tab-btn");

// 2) URL의 탭 키 ↔ 내부 인덱스 매핑 표
//   - 주소창 ?tab=interest-free 처럼 들어오면 0번 탭을 보여준다.
const TAB_INDEX = {
	"event": 0,
	"eventWinners": 1
};
//   - 반대로, 현재 0/1번 탭을 주소창에 반영할 때 쓸 키 목록
const INDEX_TAB = ["event", "eventWinners"];

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
	const key = params.get('tab') || 'event';           // ?tab=값이 없으면 기본 'interest-free'
	const index = TAB_INDEX[key] ?? 0;                          // 매핑표에 없으면 0번(첫 탭)
	showTab(index, { updateUrl });                                // 초기 표시(대개 URL 갱신은 안 함)
}

// 7) 문서가 처음 로드되면, URL 기준으로 초기 탭을 보여준다.
document.addEventListener('DOMContentLoaded', () => {
	initFromUrl();
});