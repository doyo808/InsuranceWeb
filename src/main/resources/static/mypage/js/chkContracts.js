const status_btn_active = document.getElementById('status_btn_active');
const status_btn_all = document.getElementById('status_btn_all');


status_btn_active.addEventListener('click', (e) => {
	
	if (!status_btn_active.classList.contains('btn_clicked')) {
		status_btn_active.classList.add('btn_clicked');
		status_btn_all.classList.remove('btn_clicked');		
	}
});

status_btn_all.addEventListener('click', (e) => {
	
	if (!status_btn_all.classList.contains('btn_clicked')) {
			status_btn_active.classList.remove('btn_clicked');
			status_btn_all.classList.add('btn_clicked');		
		}
});
