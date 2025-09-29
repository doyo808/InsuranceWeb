// /static/admin/js/dateRange.js
(function () {
  const ISO = d => {
    const tz = new Date(d.getTime() - d.getTimezoneOffset()*60000);
    return tz.toISOString().slice(0,10);
  };

  function setup(container) {
    const [from, to] = container.querySelectorAll('input[type="date"]');
    if (!from || !to) return;

    const today = new Date();
    const todayStr = ISO(today);

    // 오늘 이후 막기
    from.max = todayStr;
    to.max   = todayStr;

    // 초기 상호 min/max 정합
    if (from.value) to.min = from.value;
    if (to.value)   from.max = to.value;

    from.addEventListener('change', () => {
      if (from.value) {
        to.min = from.value;
        // from이 to보다 뒤면 to를 from로 맞춤
        if (to.value && to.value < from.value) to.value = from.value;
      } else {
        to.min = ''; // 해제
      }
    });

    to.addEventListener('change', () => {
      if (to.value) {
        from.max = to.value;
        // to가 from보다 앞이면 from를 to로 맞춤
        if (from.value && from.value > to.value) from.value = to.value;
      } else {
        from.max = todayStr; // 기본은 오늘
      }
    });
  }

  function initDateRange(root = document) {
    root.querySelectorAll('[data-daterange]').forEach(setup);
  }

  // auto init
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => initDateRange());
  } else {
    initDateRange();
  }

  // 전역 export(다른 페이지에서도 수동 호출 가능)
  window.appDateRange = { initDateRange };
})();
