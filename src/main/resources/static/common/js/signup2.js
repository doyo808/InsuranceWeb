document.addEventListener('DOMContentLoaded', () => {

    // =======================================================
    // 1. 폼 유효성 검사 관련 요소 및 상태
    // =======================================================
    const form = document.getElementById('signup-form-step2'); // 폼 ID를 지정해주세요.
    const checkLoginIdUrl = form.dataset.checkLoginIdUrl;

    const login_idInput = document.getElementById('login_id');
    const login_idCheckBtn = document.getElementById('login_id-check-btn');
    const login_idMessage = document.getElementById('login_id-message');

    const passwordInput = document.getElementById('password');
    const passwordCheckInput = document.getElementById('passwordCheck');
    const passwordMessage = document.getElementById('password-message');

    const submitBtn = document.getElementById('submit-btn'); // 가입 완료 버튼 ID

    const validationState = {
        login_idFormat: false, // 아이디 형식 (예: 길이)
        login_idCheckedAndAvailable: false, // 아이디 중복 확인 및 사용 가능 여부
        passwordFormat: false, // 비밀번호 형식 (길이, 조합)
        passwordMatch: false // 비밀번호 일치 여부
    };

    // --- 모든 유효성 검사 상태를 확인하여 버튼 활성화/비활성화 ---
    const checkFormValidity = () => {
        const allValid = Object.values(validationState).every(state => state === true);
        submitBtn.disabled = !allValid;
    };

    // =======================================================
    // 2. 아이디 유효성 검사
    // =======================================================

    login_idInput.addEventListener('input', () => {
        // 아이디를 수정하면 중복 확인 상태를 초기화
        validationState.login_idCheckedAndAvailable = false;
        login_idMessage.textContent = '';

        // 간단한 아이디 형식 검사 (예: 6자 이상)
        const login_id = login_idInput.value.trim();
        validationState.login_idFormat = login_id.length >= 6;
        
        checkFormValidity();
    });
    
    login_idCheckBtn.addEventListener('click', () => {
        const login_id = login_idInput.value.trim();
        if (!validationState.login_idFormat) {
            alert('아이디는 6자 이상으로 입력해주세요.');
            return;
        }

        fetch(`${checkLoginIdUrl}?login_id=${encodeURIComponent(login_id)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isAvailable) {
                    login_idMessage.textContent = '사용 가능한 아이디입니다.';
                    login_idMessage.className = 'message success';
                    validationState.login_idCheckedAndAvailable = true;
                } else {
                    login_idMessage.textContent = '이미 사용 중인 아이디입니다.';
                    login_idMessage.className = 'message error';
                    validationState.login_idCheckedAndAvailable = false;
                }
                checkFormValidity();
            })
            .catch(error => {
                console.error('Error:', error);
                login_idMessage.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                login_idMessage.className = 'message error';
                validationState.login_idCheckedAndAvailable = false;
                checkFormValidity();
            });
    });

    // =======================================================
    // 3. 비밀번호 유효성 검사
    // =======================================================

    // 비밀번호 관련 유효성을 한 번에 처리하는 함수
    const validatePassword = () => {
        const password = passwordInput.value;
        const passwordCheck = passwordCheckInput.value;
        
        // 비밀번호 형식 검사: 8~16자, 영문, 숫자, 특수문자 포함
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;
        
        if (password && !passwordRegex.test(password)) {
            passwordMessage.textContent = '비밀번호는 8~16자의 영문, 숫자, 특수문자 조합이어야 합니다.';
            passwordMessage.className = 'message error';
            validationState.passwordFormat = false;
        } else {
            // 형식이 맞으면 메시지 초기화
            passwordMessage.textContent = '';
            validationState.passwordFormat = password.length > 0; // 빈 값이 아니면 true
        }
        
        // 비밀번호 일치 여부 검사
        if (passwordCheck && password !== passwordCheck) {
            // 비밀번호 형식 메시지가 없을 때만 일치 여부 메시지 표시
            if (!passwordMessage.textContent) { 
                passwordMessage.textContent = '비밀번호가 일치하지 않습니다.';
                passwordMessage.className = 'message error';
            }
            validationState.passwordMatch = false;
        } else {
            validationState.passwordMatch = password.length > 0 && (password === passwordCheck);
        }
        
        // 두 필드가 모두 비어있다면 메시지 없음
        if (!password && !passwordCheck) {
            passwordMessage.textContent = '';
        }

        checkFormValidity();
    };

    passwordInput.addEventListener('input', validatePassword);
    passwordCheckInput.addEventListener('input', validatePassword);


    // --- 초기 상태 체크 ---
    checkFormValidity();
});