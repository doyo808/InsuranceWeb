const putContractorInfoButton = document.getElementById('bring-customer-info-button');

putContractorInfoButton.addEventListener('click', () => {
	const insuredName = document.getElementById('input_insured_name');
	insuredName.value = document.getElementById('input_customer_name').value;
	
	const insuredPN = document.getElementById('input_insured_phone_number');
	insuredPN.value = document.getElementById('input_customer_phone_number').value;
		
	const insuredEmail = document.getElementById('input_insured_email');
	insuredEmail.value = document.getElementById('input_customer_email').value;
});