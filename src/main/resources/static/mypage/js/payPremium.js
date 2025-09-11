const status_btn_continue = document.getElementById('status_btn_continue');
const status_btn_extra = document.getElementById('status_btn_extra');

const tipBox_continue = document.getElementById('tipBox_continue');
const tipBox_extra = document.getElementById('tipBox_extra');

const groupContinue = [tipBox_continue];
const groupExtra = [tipBox_extra];


status_btn_continue.addEventListener('click', (e) => {
	
	if (!status_btn_continue.classList.contains('btn_clicked')) {
		
		status_btn_continue.classList.add('btn_clicked');
		status_btn_extra.classList.remove('btn_clicked');		
		
		groupContinue.forEach((element) => {
			element.classList.remove('hidden');
		});
		groupExtra.forEach((element) => {
			element.classList.add('hidden');
		});
	}
});

status_btn_extra.addEventListener('click', (e) => {
	
	if (!status_btn_extra.classList.contains('btn_clicked')) {
			status_btn_extra.classList.add('btn_clicked');		
			status_btn_continue.classList.remove('btn_clicked');
			
			groupContinue.forEach((element) => {
				element.classList.add('hidden');
			});
			groupExtra.forEach((element) => {
				element.classList.remove('hidden');
			});
		}
});
