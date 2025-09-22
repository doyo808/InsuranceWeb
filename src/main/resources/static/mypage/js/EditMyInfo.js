document.addEventListener('DOMContentLoaded', function () {
    // ======================================
    // 주소 검색란 (Daum Postcode API)
    // ======================================
    document.querySelectorAll('.fa-magnifying-glass').forEach(icon => {
        icon.addEventListener('click', function () {
            const td = this.closest('td');
            // 데이터를 담을 input들을 선택
            const displayInput = td.querySelector('.address-display');
            const zipInput = td.querySelector('input[type="hidden"][name$="zip_code"]'); // name이 zip_code로 끝나는 hidden input
            const addr1Input = td.querySelector('input[type="hidden"][name$="address_1"], input[type="hidden"][name$="job_address1"]');

            new daum.Postcode({
                oncomplete: function(data) {
                    let fullAddr = data.address;

                    if (data.addressType === 'R') {
                        if (data.bname !== '') fullAddr += ' ' + data.bname;
                        if (data.buildingName !== '') fullAddr += ' ' + data.buildingName;
                    }
                    
                    // 3개의 input에 각각 값을 채워줌
                    displayInput.value = `(${data.zonecode}) ${fullAddr}`; // 보여주기용
                    zipInput.value = data.zonecode; // 숨겨진 우편번호
                    addr1Input.value = fullAddr; // 숨겨진 주소1
                }
            }).open();
        });
    });

    // ==============================
    // '비우기' 버튼
    // ==============================
    document.querySelectorAll('.clear-btn').forEach(button => {
        button.addEventListener('click', function () {
            const targetType = this.dataset.target;
            const table = document.querySelector(`table.custom-table[data-type="${targetType}"]`);
            if (table) {
                const inputs = table.querySelectorAll('input');
                inputs.forEach(input => {
                    input.value = '';
                });
            }
        });
    });

    // =============================
    // 이메일 중복확인 
    // =============================
    const emailCheckBtn = document.getElementById('email-check-btn');
    if (emailCheckBtn) {
        const emailMessage = document.getElementById('email-message');
        const emailInput = document.getElementById('emailInput');
        const checkEmailUrl = document.querySelector('form').dataset.checkEmailUrl;
        
        emailCheckBtn.addEventListener('click', () => {
            const email = emailInput.value;
            const emailRegex = /^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                emailMessage.textContent = '이메일 형식이 올바르지 않습니다.';
                emailMessage.style.color = 'red';
                return;
            }
            fetch(`${checkEmailUrl}?email=${encodeURIComponent(email)}`)
                .then(response => response.json())
                .then(data => {
                    if (data.isAvailable) {
                        emailMessage.textContent = '사용 가능한 이메일입니다.';
                        emailMessage.style.color = 'green';
                    } else {
                        emailMessage.textContent = '이미 사용 중인 이메일입니다.';
                        emailMessage.style.color = 'red';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    emailMessage.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                    emailMessage.style.color = 'red';
                });
        });
    }
});