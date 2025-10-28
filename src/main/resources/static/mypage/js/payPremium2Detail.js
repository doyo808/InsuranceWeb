/**
 * 보험료 납입 상세 페이지 (payPremium2Detail.html) 전용 스크립트
 * - '부분납부' 또는 '미납' 시나리오에서 포인트 계산을 처리합니다.
 */
document.addEventListener('DOMContentLoaded', () => {

    // 1. 계산 모듈이 존재하는지 확인 (부분/미납 상태일 때만 존재)
    const paymentModule = document.getElementById('payment-module');
    if (!paymentModule) {
        // '완납' 또는 '초과납부' 페이지이므로 스크립트를 실행하지 않음
        return;
    }

    // 2. 핵심 데이터 및 DOM 요소 가져오기
    const dataset = paymentModule.dataset;
    const UNPAID_AMOUNT = parseInt(dataset.unpaidAmount, 10);
    const AVAILABLE_POINTS = parseInt(dataset.availablePoints, 10);

    const pointInput = document.getElementById('points-to-use');
    const useAllPointsBtn = document.getElementById('use-all-points-btn');
    const errorMsgEl = document.getElementById('point-error-msg');
    
    const finalAmountEl = document.getElementById('final-payment-amount');
    const paymentBtnTextEl = document.getElementById('payment-btn-text');
    
    const formPointsUsedInput = document.getElementById('form-points-used');
    const formPaymentAmountInput = document.getElementById('form-payment-amount');

    // 3. 숫자 포맷 헬퍼 함수
    const formatCurrency = (num) => {
        return num.toLocaleString('ko-KR');
    };

    /**
     * 4. 메인 계산 함수
     * @param {number} pointsToUse - 사용자가 입력한 포인트
     */
    const updatePaymentDetails = (pointsToUse) => {
        let points = pointsToUse;
        let errorMessage = '';

        // 5. 유효성 검사
        // 5-1. 0 미만 입력 방지
        if (points < 0) {
            points = 0;
        }

        // 5-2. 보유 포인트 초과 사용 방지
        if (points > AVAILABLE_POINTS) {
            points = AVAILABLE_POINTS;
            errorMessage = `보유 포인트를 초과할 수 없습니다. (최대 ${formatCurrency(AVAILABLE_POINTS)} P)`;
        }

        // 5-3. 납부할 금액 초과 사용 방지
        if (points > UNPAID_AMOUNT) {
            points = UNPAID_AMOUNT;
            errorMessage = `납부할 금액을 초과할 수 없습니다. (최대 ${formatCurrency(UNPAID_AMOUNT)} P)`;
        }

        // 5-4. 에러 메시지 표시
        errorMsgEl.textContent = errorMessage;

        // 6. 최종 결제금액 계산
        const finalPayment = UNPAID_AMOUNT - points;

        // 7. DOM 업데이트
        // 7-1. (중요) 유효성 검사로 보정된 값을 input에 다시 설정
        // 사용자가 50000을 입력했어도, 10000으로 자동 보정됨
        if (pointInput.value != points && points > 0) {
             pointInput.value = points;
        } else if (points === 0 && pointInput.value !== '0' && pointInput.value !== '') {
            pointInput.value = ''; // 0이면 빈칸으로
        }
       
        // 7-2. 최종 결제금액 텍스트 업데이트
        finalAmountEl.textContent = `${formatCurrency(finalPayment)} 원`;
        
        // 7-3. 결제 버튼 텍스트 업데이트
        paymentBtnTextEl.textContent = `${formatCurrency(finalPayment)} 원 결제하기`;

        // 7-4. (중요) 서버로 전송할 hidden form 값 업데이트
        formPointsUsedInput.value = points;
        formPaymentAmountInput.value = finalPayment;
    };

    // 8. 이벤트 리스너 바인딩
    // 8-1. '전액사용' 버튼 클릭 시
    useAllPointsBtn.addEventListener('click', () => {
        // 보유 포인트와 납부할 금액 중 *더 적은 값*을 사용
        const pointsToUse = Math.min(AVAILABLE_POINTS, UNPAID_AMOUNT);
        updatePaymentDetails(pointsToUse);
    });

    // 8-2. 포인트 입력 필드에 타이핑할 때마다 (실시간)
    pointInput.addEventListener('input', (e) => {
        // 입력값이 비어있으면 0으로 간주, 아니면 숫자로 변환
        const points = e.target.value === '' ? 0 : parseInt(e.target.value, 10);
        
        if (isNaN(points)) {
            updatePaymentDetails(0);
        } else {
            updatePaymentDetails(points);
        }
    });

    // 8-3. 포인트 입력 필드에서 포커스를 잃었을 때 (숫자 보정)
    pointInput.addEventListener('change', (e) => {
         const points = e.target.value === '' ? 0 : parseInt(e.target.value, 10);
         if (isNaN(points) || points === 0) {
             pointInput.value = ''; // 포커스 잃을 때 0이면 빈칸으로
             updatePaymentDetails(0);
         } else {
             // 유효성 검사 최종 적용 (예: 999999 입력 시 최대값으로 보정)
             updatePaymentDetails(points);
         }
    });

});

