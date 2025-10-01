// =================================================================
// DOM 요소
// =================================================================
const loginBtn = document.getElementById('header__loginBtn');
const logoutBtn = document.getElementById('header__logoutBtn');
const sessionTimer = document.getElementById('header__sessionTimer');
const homeBtn = document.querySelector('.header__homeBtn');
// CSRF 토큰 가져오기 (meta 태그 또는 서버에서 내려주는 방식)
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');


// =================================================================
// 전역 변수
// =================================================================
let sessionInterval; // 타이머의 setInterval ID
const SESSION_DURATION = 30 * 60 * 1000; // 세션 유지 시간 (30분)


// =================================================================
// 이벤트 리스너
// =================================================================

// 페이지가 처음 로드될 때
window.addEventListener('load', function() {
	loginBtn.addEventListener('click', () => {
	    window.location.href = `${contextPath}login`;
	});

	logoutBtn.addEventListener('click', handleLogout);

    homeBtn.addEventListener('click', () => {
		location.href = `${contextPath}home`;
	});

    // 타이머(버튼) 클릭 시 세션 연장
    sessionTimer.addEventListener('click', () => {
        console.log('세션 시간을 연장합니다.');
        resetSession();
    });
});

// DOMContentLoaded: 로그인 상태 확인 및 초기화
document.addEventListener('DOMContentLoaded', async () => {
	const loggedIn = await checkLoginStatus();
	toggleLoginout(loggedIn);

    if (loggedIn) {
        // 로그인 상태라면 무조건 세션을 새로 시작 (새로고침 시 연장)
        resetSession();
    }
});


// =================================================================
// 함수 목록
// =================================================================

/**
 * 서버 세션을 연장하고 클라이언트 타이머를 30분으로 초기화하는 함수
 */
async function resetSession() {
    try {
        // 1. 서버에 가벼운 요청을 보내 실제 세션 만료 시간을 연장
        const response = await fetch(`${contextPath}api/auth/status`);
        if (!response.ok) {
            // 서버와 통신이 안되면 연장하지 않고 로그아웃 처리
            console.error('서버와 통신이 원활하지 않아 세션을 연장할 수 없습니다.');
            handleLogout();
            return;
        }

        // 2. 새로운 만료 시간 계산 (현재 시간 + 30분)
        const expirationTime = new Date().getTime() + SESSION_DURATION;

        // 3. 기존 타이머가 있다면 중지 (중복 실행 방지)
        if (sessionInterval) {
            clearInterval(sessionInterval);
        }

        // 4. 새 타이머 시작
        startCountdown(expirationTime);

    } catch (err) {
        console.error('세션 연장 중 오류 발생:', err);
    }
}

/**
 * 로그아웃을 처리하는 함수
 */
async function handleLogout() {
    try {	// =================  디버깅 코드 추가 =================
	        console.log('--- 로그아웃 요청 직전 값 확인 ---');
	        console.log('URL:', `${contextPath}logout`);
	        console.log('Header Name (csrfHeader):', csrfHeader);
	        console.log('Header Value (csrfToken):', csrfToken);
	        // =================================================
        const response = await fetch(`${contextPath}logout`, {
            method: 'POST',
            headers: { 
				[csrfHeader]: csrfToken 
					}
        });
        
        if (response.ok) {
            clearInterval(sessionInterval);
            toggleLoginout(false);
            window.location.href = `${contextPath}home`;
        } else {
            alert('로그아웃 실패');
        }
    } catch (err) {
        console.error('로그아웃 중 오류 발생:', err);
    }
}

/**
 * 화면에 남은 시간을 표시하는 카운트다운을 시작하는 함수
 * @param {number} expirationTime - 세션이 만료되는 시간 (타임스탬프)
 */
function startCountdown(expirationTime) {
    sessionInterval = setInterval(() => {
        const now = new Date().getTime();
        const distance = expirationTime - now;

        // 시간이 다 되면 타이머를 멈추고 만료 알림 후 로그아웃
        if (distance <= 0) {
            alert('세션이 만료되었습니다. 다시 로그인해주세요.');
            handleLogout(); // 로그아웃 함수 호출
            return;
        }

        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        sessionTimer.textContent = `V ${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')} 로그인연장`;
    }, 1000);
}


/**
 * 로그인/로그아웃 상태에 따라 버튼 표시 여부를 토글하는 함수
 * @param {boolean} isLoggedIn 
 */
function toggleLoginout(isLoggedIn) {
    loginBtn.classList.toggle('hidden', isLoggedIn);
    logoutBtn.classList.toggle('hidden', !isLoggedIn);
    sessionTimer.classList.toggle('hidden', !isLoggedIn);    
};

/**
 * 서버에 로그인 상태를 확인하고 결과를 반환하는 함수
 * @returns {Promise<boolean>}
 */
async function checkLoginStatus() {
	try {
		const response = await fetch(`${contextPath}api/auth/status`);
		if (response.ok) {
			const data = await response.json();
			return data.loggedIn === true;
		}
	} catch (err) {
		console.log('로그인 상태 확인 실패:', err);
	}
	return false;
}