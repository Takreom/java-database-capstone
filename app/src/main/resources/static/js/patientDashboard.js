import { createDoctorCard } from './components/doctorCard.js';
import { openModal } from './components/modals.js';
import { filterDoctors, getDoctors } from './services/doctorServices.js';
import { patientLogin, patientSignup } from './services/patientServices.js';

document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();

    document.getElementById('patientSignup')?.addEventListener('click', () => openModal('patientSignup'));
    document.getElementById('patientLogin')?.addEventListener('click', () => openModal('patientLogin'));
    document.getElementById('searchBar')?.addEventListener('input', filterDoctorsOnChange);
    document.getElementById('filterTime')?.addEventListener('change', filterDoctorsOnChange);
    document.getElementById('filterSpecialty')?.addEventListener('change', filterDoctorsOnChange);
});

async function loadDoctorCards() {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
}

async function filterDoctorsOnChange() {
    const name = document.getElementById('searchBar')?.value.trim() || null;
    const time = document.getElementById('filterTime')?.value || null;
    const specialty = document.getElementById('filterSpecialty')?.value || null;
    const response = await filterDoctors(name, time, specialty);
    renderDoctorCards(response.doctors || []);
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById('content');
    if (!contentDiv) return;

    contentDiv.innerHTML = '';

    if (!doctors.length) {
        contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
        return;
    }

    doctors.forEach((doctor) => contentDiv.appendChild(createDoctorCard(doctor)));
}

window.signupPatient = async function () {
    const data = {
        name: document.getElementById('name')?.value,
        email: document.getElementById('email')?.value,
        password: document.getElementById('password')?.value,
        phone: document.getElementById('phone')?.value,
        address: document.getElementById('address')?.value
    };

    const result = await patientSignup(data);
    alert(result.message);

    if (result.success) {
        document.getElementById('modal').style.display = 'none';
        window.location.reload();
    }
};

window.loginPatient = async function () {
    const data = {
        email: document.getElementById('email')?.value,
        password: document.getElementById('password')?.value
    };

    try {
        const response = await patientLogin(data);
        if (!response.ok) {
            alert('Invalid credentials!');
            return;
        }

        const result = await response.json();
        localStorage.setItem('token', result.token);
        selectRole('loggedPatient');
        window.location.href = '/pages/loggedPatientDashboard.html';
    } catch (error) {
        console.error('Patient login failed:', error);
        alert('Unable to log in as patient.');
    }
};
