import { API_BASE_URL } from '../config/config.js';

const DOCTOR_API = API_BASE_URL + '/doctor';

export async function getDoctors() {
    try {
        const response = await fetch(DOCTOR_API);
        if (!response.ok) return [];
        const data = await response.json();
        return data.doctors || [];
    } catch (error) {
        console.error('Failed to fetch doctors:', error);
        return [];
    }
}

export async function deleteDoctor(id, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${id}/${token}`, { method: 'DELETE' });
        const data = await response.json().catch(() => ({}));
        return {
            success: response.ok,
            message: data.message || (response.ok ? 'Doctor deleted successfully.' : 'Unable to delete doctor.')
        };
    } catch (error) {
        console.error('Failed to delete doctor:', error);
        return { success: false, message: 'Unable to delete doctor.' };
    }
}

export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${token}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor)
        });
        const data = await response.json().catch(() => ({}));
        return {
            success: response.ok,
            message: data.message || (response.ok ? 'Doctor saved successfully.' : 'Unable to save doctor.')
        };
    } catch (error) {
        console.error('Failed to save doctor:', error);
        return { success: false, message: 'Unable to save doctor.' };
    }
}

export async function filterDoctors(name, time, specialty) {
    const safeName = encodeURIComponent(name || 'null');
    const safeTime = encodeURIComponent(time || 'null');
    const safeSpecialty = encodeURIComponent(specialty || 'null');

    try {
        const response = await fetch(`${DOCTOR_API}/filter/${safeName}/${safeTime}/${safeSpecialty}`);
        if (!response.ok) return { doctors: [] };
        const data = await response.json();
        return { doctors: data.doctors || [] };
    } catch (error) {
        console.error('Failed to filter doctors:', error);
        return { doctors: [] };
    }
}
