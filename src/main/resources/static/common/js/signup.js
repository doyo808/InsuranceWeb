
document.addEventListener('DOMContentLoaded', () => {

    // =======================================================
    // 1. 폼 유효성 검사 관련 요소 및 상태
    // =======================================================
    const form = document.getElementById('signup-form');
	const checkEmailUrl = form.dataset.checkEmailUrl;
    const korNameInput = document.getElementById('korName');
    const jumin6Input = document.getElementById('jumin6');
    const jumin7Input = document.getElementById('jumin7');
    const phoneInput = document.getElementById('phoneNumber');
    const emailInput = document.getElementById('email');
    const emailCheckBtn = document.getElementById('email-check-btn');
    const emailMessage = document.getElementById('email-message');
    const agreementCheck = document.getElementById('main-agreement');
    const nextBtn = document.getElementById('signup-next-btn');

    const validationState = {
        korName: false,
        jumin: false,
        phone: false,
        email: false,
        emailCheckedAndAvailable: false,
        agreement: false,
		cert: false
    };

    const checkFormValidity = () => {
        const allValid = Object.values(validationState).every(state => state === true);
        nextBtn.disabled = !allValid;
    };
    
    // --- 각 입력 필드에 대한 실시간 유효성 검사 ---
    korNameInput.addEventListener('input', () => {
        validationState.korName = korNameInput.value.trim().length > 1;
        checkFormValidity();
    });

    const checkJumin = () => {
        validationState.jumin = jumin6Input.value.trim().length === 6 && jumin7Input.value.trim().length === 7;
        checkFormValidity();
    };
    jumin6Input.addEventListener('input', checkJumin);
    jumin7Input.addEventListener('input', checkJumin); 

    phoneInput.addEventListener('input', () => {
        validationState.phone = phoneInput.value.trim().length >= 10;
        checkFormValidity();
    });

    emailInput.addEventListener('input', () => {
        validationState.email = /@/.test(emailInput.value);
        validationState.emailCheckedAndAvailable = false;
        emailMessage.textContent = '';
        checkFormValidity();
    });

    agreementCheck.addEventListener('change', () => {
        validationState.agreement = agreementCheck.checked;
        checkFormValidity();
    });

    // --- 이메일 중복 확인 버튼 클릭 이벤트 ---
    emailCheckBtn.addEventListener('click', () => {
        const email = emailInput.value;
        if (!validationState.email) {
            alert('올바른 이메일 형식을 입력해주세요.');
            return;
        }

		fetch(`${checkEmailUrl}?email=${encodeURIComponent(email)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isAvailable) {
                    emailMessage.textContent = '사용 가능한 이메일입니다.';
                    emailMessage.style.color = 'green';
                    validationState.emailCheckedAndAvailable = true;
                } else {
                    emailMessage.textContent = '이미 사용 중인 이메일입니다.';
                    emailMessage.style.color = 'red';
                    validationState.emailCheckedAndAvailable = false;
                }
                checkFormValidity(); 
            })
            .catch(error => {
                console.error('Error:', error);
                emailMessage.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                emailMessage.style.color = 'red';
                validationState.emailCheckedAndAvailable = false;
                checkFormValidity();
            });
    });
    
    // =======================================================
    // 2. 약관 동의 모달 관련 요소 및 로직
    // =======================================================
    const viewDetailsBtn = document.getElementById('view-details-btn');
    const agreementModal = document.getElementById('agreement-modal');
    const closeBtn = agreementModal.querySelector('.close-btn');
    const overlay = agreementModal.querySelector('.modal-overlay');
    const confirmBtn = document.getElementById('confirm-btn');
    const radioGroups = agreementModal.querySelectorAll('input[type="radio"]');

    const openModal = () => agreementModal.classList.remove('hidden');
    const closeModal = () => agreementModal.classList.add('hidden');

    viewDetailsBtn.addEventListener('click', openModal);
    closeBtn.addEventListener('click', closeModal);
    overlay.addEventListener('click', closeModal);

    const checkConsentInModal = () => {
        const idConsent = agreementModal.querySelector('input[name="consent-id"]:checked').value;
        const piConsent = agreementModal.querySelector('input[name="consent-pi"]:checked').value;
        confirmBtn.disabled = !(idConsent === 'agree' && piConsent === 'agree');
    };

    radioGroups.forEach(radio => {
        radio.addEventListener('change', checkConsentInModal);
    });

    confirmBtn.addEventListener('click', () => {
        if (!confirmBtn.disabled) {
            agreementCheck.checked = true; // 메인 동의 체크박스를 체크
            
            // 중요: 강제로 change 이벤트를 발생시켜 validationState를 업데이트하고
            // '다음' 버튼 활성화 여부를 다시 체크하도록 함
            agreementCheck.dispatchEvent(new Event('change'));
            
            closeModal();
        }
    });

    // 초기 상태 체크
    checkFormValidity();
	
	
	// 아임포트 초기화 (테스트용 개발 가맹점 ID)
	var IMP = window.IMP;
	IMP.init("imp72048686");

	// 버튼 클릭 시 본인인증 호출
	document.getElementById("certBtn").addEventListener("click", function() {
	  IMP.certification({
	    // 주문 번호
	    merchant_uid: "ORD20180131-0000011",
	  }, function(rsp) {
		  cert = true;
		  checkFormValidity();
		  
		  document.getElementById("certSuccessModal").style.display = "block";
            setTimeout(() => { 
              document.getElementById("certSuccessModal").style.display = "none"; 
            }, 2000);
	      // 서버로 전달 
	    if (rsp.success) {
	      fetch(`${contextPath}cert/complete`, {
	        method: "POST",
	        headers: { "Content-Type": "application/json" },
	        body: JSON.stringify(rsp)
	      });
	    } else {
	      alert("본인인증 실패: " + rsp.error_msg);
	    }
	  });
	});

	
	
});