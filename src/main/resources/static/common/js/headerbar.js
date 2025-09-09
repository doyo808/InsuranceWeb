document.addEventListener('DOMContentLoaded', function() {
	const loginBtn = document.getElementById('loginBtn');
	const logoutBtn = document.getElementById('logoutBtn');
	const sessionTimer = document.getElementById('sessionTimer');
	
	loginBtn.addEventListener('click', (e) => {
	    togglelogin_out();
	});
	logoutBtn.addEventListener('click', (e) => {
	    togglelogin_out();
	})
	
	function togglelogin_out() {
	    loginBtn.classList.toggle('hidden');
	    logoutBtn.classList.toggle('hidden');
	    sessionTimer.classList.toggle('hidden');    
	};
});