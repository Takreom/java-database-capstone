import { API_BASE_URL } from '../config/config.js';

const PATIENT_API = API_BASE_URL + '/patient';

async function readJson(response) {
    try {
        return await response.json();
    } catch {
        return {};
    }
}

export async function patientSignup(data) {
    try {
        const response = await fetch(PATIENT_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await readJson(response);

        return {
            success: response.ok,
            message: result.message || (response.ok ? 'Patient registered successfully.' : 'Unable to register patient.')
        };
    } catch (error) {
        console.error('Patient signup failed:', error);
        return { success: false, message: 'Network error while registering patient.' };
    }
}

export async function patientLogin(data) {
    return fetch(`${PATIENT_API}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
}

export async function getPatientData(token) {
    try {
        const response = await fetch(`${PATIENT_API}/${token}`);
        const data = await readJson(response);
        return response.ok ? data.patient : null;
    } catch (error) {
        console.error('Error fetching patient details:', error);
        return null;
    }
}

export async function getPatientAppointments(id, token, user) {
    try {
        const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
        const data = await readJson(response);
        return response.ok ? data.appointments || [] : [];
    } catch (error) {
        console.error('Error fetching patient appointments:', error);
        return [];
    }
}

export async function filterAppointments(condition, name, token) {
    const safeCondition = encodeURIComponent(condition || 'all');
    const safeName = encodeURIComponent(name || 'null');

    try {
        const response = await fetch(`${PATIENT_API}/filter/${safeCondition}/${safeName}/${token}`);
        const data = await readJson(response);
        return response.ok ? data : { appointments: [] };
    } catch (error) {
        console.error('Error filtering appointments:', error);
        return { appointments: [] };
    }
}
