
document.addEventListener('DOMContentLoaded', () => {

	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const csrfParameterName = '_csrf'; 
	
    // th:each로 생성된 모든 contract-row 요소에 이벤트 리스너 추가
    document.querySelectorAll('.contract-row').forEach(row => {
        row.addEventListener('click', () => {
            
            // 1. 클릭한 행(row)의 dataset에서 모든 정보 추출
            const dataset = row.dataset;
            const contract_id = dataset.contractId; // data-contract-id

            // 2. 동적으로 form 요소 생성
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = `${contextPath}mypage/MPDG0080/${contract_id}`;
            form.style.display = 'none'; 

            // 3. dataset의 값들을 hidden input으로 생성하여 form에 추가
            const fields = {
				'payment_id': dataset.paymentId,
                'product_name': dataset.productName,
                'payment_date': dataset.paymentDate,
                'paid_amount': dataset.paidAmount,
                'total_premium': dataset.totalPremium,
                'pay_status': dataset.payStatus
            };

            for (const key in fields) {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = key;
                input.value = fields[key];
                form.appendChild(input);
            }
			
			if (csrfToken) {
			    const csrfInput = document.createElement('input');
			    csrfInput.type = 'hidden';
			    csrfInput.name = csrfParameterName;
			    csrfInput.value = csrfToken;
			    form.appendChild(csrfInput);
			}

            // 4. form을 body에 추가하고 즉시 submit
            document.body.appendChild(form);
            form.submit();
        });
    });

});

