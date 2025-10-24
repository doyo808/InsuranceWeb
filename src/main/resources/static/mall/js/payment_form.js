// ============================
// 포트원 결제 JS
// ============================

// 페이지 로드 변수설정
IMP.init("imp72048686"); //  (실서비스용)
// IMP.init("imp10391932"); // 포트원 공식 테스트 ID


const profile = document.body.dataset.profile;
const productName = document.querySelector("main").dataset.productName;
const customerName = document.querySelector("main").dataset.customerName;
const premium = profile === "dev" ? 1 : parseInt(document.querySelector("main").dataset.premium, 10);

const tokenMeta = document.querySelector('meta[name="_csrf"]');
const headerMeta = document.querySelector('meta[name="_csrf_header"]');
const token = tokenMeta ? tokenMeta.content : '';
const header = headerMeta ? headerMeta.content : '';

// ============================
// 결제 요청 함수
// ============================
function requestPay() {
    const merchant_uid = "ORD" + new Date().getTime(); // 유니크 주문번호

    const paymentParams = {		
        pg: "html5_inicis",          		 // 테스트 PG
        pay_method: "card",          		 // 결제 수단
        merchant_uid: merchant_uid,  		 // 주문번호
        name: productName, 			 		 // 주문명
        amount: premium,               		 // 테스트 결제금액
        buyer_name: customerName
    };

    IMP.request_pay(paymentParams, async function(rsp) {
        if (rsp.success) {
            console.log("결제 성공! 검증을 시작합니다.");

            // 개발 환경이면 서버 검증 없이 바로 처리
            if (profile === "dev1") {
                handlePaymentSuccess(rsp);
				activateNextBtn();
                return;
            }

            // 운영 환경이면 서버 검증 호출
			const verified = await verifyPaymentOnServer(rsp.imp_uid, rsp.merchant_uid, rsp.amount);
			if (verified) { activateNextBtn(); } 
        } else {
            alert("결제에 실패했습니다. 에러: " + rsp.error_msg);
        }
    });
}

// ============================
// 서버 검증 함수
// ============================
async function verifyPaymentOnServer(imp_uid, merchant_uid, amount) {
    try {
        const response = await fetch('payment/verify', {
            method: 'POST',
            headers: { 
				'Content-Type': 'application/json',
				[header]: token
			},
            body: JSON.stringify({ imp_uid, merchant_uid, amount })
        });

        const isVerified = await response.json();

        if (isVerified) {
            handlePaymentSuccess({ imp_uid, merchant_uid, amount });
        } else {
            handlePaymentFailure("서버 검증 실패. 관리자에게 문의하세요.");
        }
    } catch (error) {
        console.error('서버 통신 오류:', error);
        handlePaymentFailure("서버와 통신 중 오류 발생");
    }
}

// ============================
// 결제 성공 처리
// ============================
function handlePaymentSuccess(rsp) {
    alert("결제 성공 및 검증 완료!");
    console.log("결제 정보:", rsp);
    // TODO: 결제 성공 페이지 이동 또는 UI 업데이트
}

// ============================
// 결제 실패 처리
// ============================
function handlePaymentFailure(message) {
    alert("결제 실패: " + message);
    // TODO: 필요 시 결제 취소 API 호출 등 후속 조치
}

// 계약완료버튼 활성화
function activateNextBtn() {
	const nextBtn = document.querySelector(".next-btn");
	nextBtn.disabled = false;
	nextBtn.style.cursor = 'pointer';
}
