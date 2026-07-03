import { createPatientRow } from './components/patientRows.js';
import { getAllAppointments } from './services/appointmentRecordService.js';

const today = new Date().toISOString().split('T')[0];
let selectedDate = today;
let patientName = 'null';

document.addEventListener('DOMContentLoaded', () => {
    const datePicker = document.getElementById('datePicker');
    if (datePicker) datePicker.value = selectedDate;

    document.getElementById('searchBar')?.addEventListener('input', (event) => {
        const value = event.target.value.trim();
        patientName = value || 'null';
        loadAppointments();
    });

    document.getElementById('todayButton')?.addEventListener('click', () => {
        selectedDate = today;
        if (datePicker) datePicker.value = today;
        loadAppointments();
    });

    datePicker?.addEventListener('change', (event) => {
        selectedDate = event.target.value || today;
        loadAppointments();
    });

    loadAppointments();
});

async function loadAppointments() {
    const tableBody = document.getElementById('patientTableBody');
    if (!tableBody) return;

    const token = localStorage.getItem('token');
    tableBody.innerHTML = '<tr><td colspan="5" class="noPatientRecord">Loading appointments...</td></tr>';

    try {
        const response = await getAllAppointments(selectedDate, patientName, token);
        const appointments = Array.isArray(response) ? response : response.appointments || [];

        tableBody.innerHTML = '';

        if (!appointments.length) {
            tableBody.innerHTML = '<tr><td colspan="5" class="noPatientRecord">No appointments found for the selected date.</td></tr>';
            return;
        }

        appointments.forEach((appointment) => {
            const patient = appointment.patient || {
                id: appointment.patientId || 'N/A',
                name: appointment.patientName || 'Unknown Patient',
                phone: appointment.patientPhone || 'N/A',
                email: appointment.patientEmail || 'N/A'
            };
            const doctorId = appointment.doctor?.id || appointment.doctorId;
            tableBody.appendChild(createPatientRow(patient, appointment.id, doctorId));
        });
    } catch (error) {
        console.error('Error loading appointments:', error);
        tableBody.innerHTML = '<tr><td colspan="5" class="noPatientRecord">Error loading appointments. Try again later.</td></tr>';
    }
}
