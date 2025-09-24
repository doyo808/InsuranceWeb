const sidebar = document.getElementById('sidebar');
const toggleBtn = document.getElementById('toggleBtn');

const side_menu3 = document.getElementById('side_menu3'); 

toggleBtn.addEventListener('click', () => {
sidebar.classList.toggle('closed');
sidebar.classList.toggle('open');

if (sidebar.classList.contains('closed')) {
    toggleBtn.innerHTML = '<img src="/kdInsuranceTeam/common/images/btn_side_off.png"></img>';
} else {
    toggleBtn.innerHTML = '<img src="/kdInsuranceTeam/common/images/btn_side.png"></img>';
}
});


function showContainer () {

}

side_menu3.addEventListener('click', (e) => {
    showContainer
});


// ----------------------------------------------------
    
  const mainContainer = document.getElementById('baseContainer3');

  async function loadInto(container, url, tab) {
    try {
      const res = await fetch(url, { credentials: 'same-origin' });
      const html = await res.text();

      // 1) 통째로 넣기(가장 간단)
      container.innerHTML = html;

      // 2) 특정 영역만 뽑아 넣고 싶으면 DOM 파싱해서 추출
      // const doc = new DOMParser().parseFromString(html, 'text/html');
      // const fragment = doc.querySelector('#benefit-root') || doc.body; // 대상 선택자
      // container.innerHTML = fragment.innerHTML;

      // 탭 선택이 필요하다면, 불러온 문서 내부의 함수/요소를 찾아서 활성화
      if (tab) {
        // 예: 불러온 문서가 전역 함수 openTab('installment') 를 제공한다고 가정
        // 또는 탭 버튼을 querySelector로 찾아 클릭 트리거
        const tryOpen = () => {
          // 케이스 A: 전역 함수
          if (typeof window.openBenefitTab === 'function') {
            window.openBenefitTab(tab);
            return true;
          }
          // 케이스 B: 탭 버튼 클릭
          const btn = container.querySelector(`[data-tab="${tab}"]`);
          if (btn) { btn.click(); return true; }
          return false;
        };

        // DOM 붙은 뒤 바로 시도
        tryOpen();

        // 혹시 지연 초기화가 있으면 한 번 더 시도(옵션)
        setTimeout(tryOpen, 50);
      }

      // history 갱신(뒤로 가기 지원)
      history.pushState({ url, tab }, '', `#${tab || 'content'}`);
    } catch (e) {
      console.error(e);
      container.innerHTML = '<p>콘텐츠를 불러오지 못했습니다.</p>';
    }
  }

  // 사이드 링크 공통 핸들러
  document.querySelectorAll('.side_link').forEach(a => {
    a.addEventListener('click', (e) => {
      e.preventDefault();
      const url = a.dataset.route;
      const tab = a.dataset.tab; // 예: 'installment'
      if (url) loadInto(mainContainer, url, tab);
    });
  });

  // 뒤로가기 처리
  window.addEventListener('popstate', (ev) => {
    if (ev.state?.url) {
      loadInto(mainContainer, ev.state.url, ev.state.tab);
    }
  });
