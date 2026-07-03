import { deleteDoctor } from '../services/doctorServices.js';

export function createDoctorCard(doctor) {
    const card = document.createElement('article');
    card.classList.add('doctor-card');

    const role = localStorage.getItem('userRole');
    const availableTimes = Array.isArray(doctor.availableTimes)
        ? doctor.availableTimes.join(', ')
        : doctor.availableTimes || 'No availability listed';

    const infoDiv = document.createElement('div');
    infoDiv.classList.add('doctor-info');
    infoDiv.innerHTML = `
        <h3>${doctor.name || 'Unknown Doctor'}</h3>
        <p><strong>Specialty:</strong> ${doctor.specialty || doctor.specialization || 'Not specified'}</p>
        <p><strong>Email:</strong> ${doctor.email || 'Not available'}</p>
        <p><strong>Phone:</strong> ${doctor.phone || 'Not available'}</p>
        <p><strong>Availability:</strong> ${availableTimes}</p>`;

    const actionsDiv = document.createElement('div');
    actionsDiv.classList.add('card-actions');

    if (role === 'admin') {
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.textContent = 'Delete';
        removeBtn.addEventListener('click', async () => {
            if (!confirm(`Delete ${doctor.name}?`)) return;
            const token = localStorage.getItem('token');
            const result = await deleteDoctor(doctor.id, token);
            alert(result.message || (result.success ? 'Doctor deleted.' : 'Unable to delete doctor.'));
            if (result.success) card.remove();
        });
        actionsDiv.appendChild(removeBtn);
    } else if (role === 'patient') {
        const bookNow = document.createElement('button');
        bookNow.type = 'button';
        bookNow.textContent = 'Book Now';
        bookNow.addEventListener('click', () => alert('Please log in before booking an appointment.'));
        actionsDiv.appendChild(bookNow);
    } else if (role === 'loggedPatient') {
        const bookNow = document.createElement('button');
        bookNow.type = 'button';
        bookNow.textContent = 'Book Now';
        bookNow.addEventListener('click', () => alert('Booking workflow will be completed in the services lab.'));
        actionsDiv.appendChild(bookNow);
    }

    card.appendChild(infoDiv);
    if (actionsDiv.children.length > 0) card.appendChild(actionsDiv);
    return card;
}
