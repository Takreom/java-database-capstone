function getAssetPrefix() {
    return window.location.pathname.includes('/pages/') ? '..' : '';
}

function renderHeader() {
    const headerDiv = document.getElementById('header');
    if (!headerDiv) return;

    const path = window.location.pathname;
    const isHome = path === '/' || path.endsWith('/index.html');
    const prefix = getAssetPrefix();

    if (isHome) {
        localStorage.removeItem('userRole');
        localStorage.removeItem('token');
    }

    const role = localStorage.getItem('userRole');
    const token = localStorage.getItem('token');

    if ((role === 'loggedPatient' || role === 'admin' || role === 'doctor') && !token && !isHome) {
        localStorage.removeItem('userRole');
        alert('Session expired or invalid login. Please log in again.');
        window.location.href = '/';
        return;
    }

    let navContent = '';

    if (role === 'admin') {
        navContent = `
            <button id="addDocBtn" class="adminBtn" type="button">Add Doctor</button>
            <a href="#" id="logoutLink">Logout</a>`;
    } else if (role === 'doctor') {
        navContent = `
            <button class="adminBtn" type="button" onclick="selectRole('doctor')">Home</button>
            <a href="#" id="logoutLink">Logout</a>`;
    } else if (role === 'patient') {
        navContent = `
            <button id="patientLogin" class="adminBtn" type="button">Login</button>
            <button id="patientSignup" class="adminBtn" type="button">Sign Up</button>`;
    } else if (role === 'loggedPatient') {
        navContent = `
            <button class="adminBtn" type="button" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
            <button class="adminBtn" type="button" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
            <a href="#" id="logoutPatientLink">Logout</a>`;
    }

    headerDiv.innerHTML = `
        <header class="header">
            <a class="logo-link" href="/" aria-label="Smart Clinic home">
                <img src="${prefix}/assets/images/logo/logo.png" alt="Smart Clinic Logo" class="logo-img">
                <span class="logo-title">Smart Clinic</span>
            </a>
            <nav>${navContent}</nav>
        </header>`;

    attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
    const addDocBtn = document.getElementById('addDocBtn');
    if (addDocBtn) {
        addDocBtn.addEventListener('click', () => {
            if (window.openModal) window.openModal('addDoctor');
        });
    }

    const patientLogin = document.getElementById('patientLogin');
    if (patientLogin) {
        patientLogin.addEventListener('click', () => {
            if (window.openModal) window.openModal('patientLogin');
        });
    }

    const patientSignup = document.getElementById('patientSignup');
    if (patientSignup) {
        patientSignup.addEventListener('click', () => {
            if (window.openModal) window.openModal('patientSignup');
        });
    }

    const logoutLink = document.getElementById('logoutLink');
    if (logoutLink) logoutLink.addEventListener('click', logout);

    const logoutPatientLink = document.getElementById('logoutPatientLink');
    if (logoutPatientLink) logoutPatientLink.addEventListener('click', logoutPatient);
}

function logout(event) {
    if (event) event.preventDefault();
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    window.location.href = '/';
}

function logoutPatient(event) {
    if (event) event.preventDefault();
    localStorage.removeItem('token');
    localStorage.setItem('userRole', 'patient');
    window.location.href = '/pages/patientDashboard.html';
}

renderHeader();
