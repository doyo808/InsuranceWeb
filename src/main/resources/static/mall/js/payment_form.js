// 페이지가 로드되면 포트원 라이브러리를 초기화
// "가맹점 식별코드"는 포트원 관리자 콘솔에서 확인 가능
IMP.init("imp72048686"); // TODO: 본인의 가맹점 식별코드로 변경

function requestPay() {
    // 1. 고유한 주문번호와 빌링키(고객번호) 생성
    // 실제로는 회원 ID나 UUID 등을 조합하여 유니크하게 만들어야 함
    const merchant_uid = "ORD" + new Date().getTime(); // 예: ORD17275996...

    // 2. IMP.request_pay(param, callback) 호출
    IMP.request_pay({
        pg: "html5_inicis",           // PG사 (테스트: html5_inicis)
        pay_method: "card",           // 결제수단
        merchant_uid: merchant_uid,   // 영수증 번호 (고유해야 함)
        name: "든든 건강보험 (첫 결제)", // 주문명
        amount: 100,                  // 결제금액 (테스트용)
        buyer_name: "홍길동",
        buyer_tel: "010-1234-5678",
    }, function (rsp) { // callback
        if (rsp.success) {
            // 3. 결제 성공 시, 백엔드에 결제 검증 요청
            console.log("결제 성공! 검증을 시작합니다.");
            verifyPaymentOnServer(rsp.imp_uid, rsp.merchant_uid);
        } else {
            alert("결제에 실패했습니다. 에러: " + rsp.error_msg);
        }
    });
}

// 백엔드 서버에 결제 검증을 요청하는 함수
async function verifyPaymentOnServer(imp_uid, merchant_uid) {
    try {
        const response = await fetch('/payment/verify', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                imp_uid: imp_uid,
                merchant_uid: merchant_uid
            })
        });

        const isVerified = await response.json();

        if (isVerified) {
            alert("결제 성공 및 검증 완료! 정기결제가 등록되었습니다.");
            // TODO: 결제 성공 페이지로 이동 또는 UI 업데이트
        } else {
            alert("결제는 성공했으나 서버 검증에 실패했습니다. 관리자에게 문의하세요.");
            // TODO: 위변조 의심 상황이므로 결제 취소 API를 호출하는 등의 후속 조치 필요
        }
    } catch (error) {
        console.error('서버 통신 중 오류 발생:', error);
        alert('서버와 통신 중 오류가 발생했습니다.');
    }
}