
// 1. 요소 선택
const status_btn_active = document.getElementById('status_btn_active');
const status_btn_all = document.getElementById('status_btn_all');
const activeTable = document.getElementById('active-contracts-table');
const allTable = document.getElementById('all-contracts-table');

// 2. '유지' 버튼 클릭 이벤트
status_btn_active.addEventListener('click', () => {
	
	// 버튼 스타일 변경
	if (!status_btn_active.classList.contains('btn_clicked')) {
		status_btn_active.classList.add('btn_clicked');
		status_btn_all.classList.remove('btn_clicked');		
	}
	
	// 테이블 보이기/숨기기
	activeTable.style.display = 'table'; // '유지' 테이블 보이기
	allTable.style.display = 'none';     // '전체' 테이블 숨기기
});

// 3. '전체' 버튼 클릭 이벤트
status_btn_all.addEventListener('click', () => {
	
	// 버튼 스타일 변경
	if (!status_btn_all.classList.contains('btn_clicked')) {
		status_btn_active.classList.remove('btn_clicked');
		status_btn_all.classList.add('btn_clicked');		
	}
	
	// 테이블 보이기/숨기기
	activeTable.style.display = 'none';      // '유지' 테이블 숨기기
	allTable.style.display = 'table';      // '전체' 테이블 보이기
});


// 각 계약행을 요소로 선택
document.querySelectorAll('.contract-row').forEach(row => {
    row.addEventListener('click', () => {
        const contract_id = row.dataset.contractId;
        window.location.href = `${contextPath}mypage/MPDG0071/${contract_id}`;
    });
});