$(document).ready(function () {
  // 탭 버튼 클릭 처리
  $('.tab-btn').on('click', function () {
    const target = $(this).data('tab');

    // 버튼 활성화 처리
    $('.tab-btn').removeClass('active');
    $(this).addClass('active');

    // 탭 내용 표시 처리
    $('.tab-content').each(function () {
      if ($(this).attr('id') === target) {
        $(this).show();
      } else {
        $(this).hide();
      }
    });
  });

  // 운영체제 확인
  function getOS() {
    const platform = navigator.platform.toLowerCase();
    const userAgent = navigator.userAgent.toLowerCase();

    if (platform.includes("win")) return "Windows";
    if (platform.includes("mac")) return "macOS";
    if (platform.includes("linux")) return "Linux";
    if (/android/.test(userAgent)) return "Android";
    if (/iphone|ipad|ipod/.test(userAgent)) return "iOS";
    return "Unknown";
  }

  // 브라우저 확인
  function getBrowser() {
    const ua = navigator.userAgent.toLowerCase();

    if (ua.includes("edg")) return "Microsoft Edge";
    if (ua.includes("chrome") && !ua.includes("edg")) return "Chrome";
    if (ua.includes("safari") && !ua.includes("chrome")) return "Safari";
    if (ua.includes("firefox")) return "Firefox";
    if (ua.includes("trident") || ua.includes("msie")) return "Internet Explorer";
    return "Unknown";
  }

  // 사용자 정보 표시
  $('#os-info').text(getOS());
  $('#browser-info').text(getBrowser());
});
