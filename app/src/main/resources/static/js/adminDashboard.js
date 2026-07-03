import { openModal } from './components/modals.js';
import { createDoctorCard } from './components/doctorCard.js';
import { filterDoctors, getDoctors, saveDoctor } from './services/doctorServices.js';

document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();

    document.getElementById('searchBar')?.addEventListener('input', filterDoctorsOnChange);
    document.getElementById('filterTime')?.addEventListener('change', filterDoctorsOnChange);
    document.getElementById('filterSpecialty')?.addEventListener('change', filterDoctorsOnChange);

    document.getElementById('addDocBtn')?.addEventListener('click', () => openModal('addDoctor'));
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

window.adminAddDoctor = async function () {
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Admin token not found. Please log in again.');
        return;
    }

    const availableTimes = Array.from(document.querySelectorAll('input[name="availability"]:checked'))
        .map((input) => input.value);

    const doctor = {
        name: document.getElementById('doctorName')?.value,
        specialty: document.getElementById('specialization')?.value,
        email: document.getElementById('doctorEmail')?.value,
        password: document.getElementById('doctorPassword')?.value,
        phone: document.getElementById('doctorPhone')?.value,
        availableTimes
    };

    const result = await saveDoctor(doctor, token);
    alert(result.message);

    if (result.success) {
        document.getElementById('modal').style.display = 'none';
        loadDoctorCards();
    }
};
