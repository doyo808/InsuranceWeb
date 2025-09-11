window.addEventListener('load', function() {
	const loginBtn = document.getElementById('header__loginBtn');
	const logoutBtn = document.getElementById('header__logoutBtn');
	const sessionTimer = document.getElementById('header__sessionTimer');
	
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