const API_BASE_URL = '/mypage/api/marketing';

/**
 * 현재 사용자의 동의 상태를 '조회'하는 함수
 * @returns {Promise<object>} 동의 상태 DTO
 */
export async function fetchConsentStatus() {
	const response = await fetch(`${API_BASE_URL}/status`);
	if (!response.ok) {
		throw new Error('Failed to fetch consent status');
	}
	return response.json();
}

/**
 * 사용자의 동의 상태를 '저장'하는 함수
 * @param {object} dto - 서버로 보낼 동의 상태 DTO
 * @returns {Promise<Response>} fetch 응답 객체
 */