package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
                              DoctorRepository doctorRepository, PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            hydrateRelations(appointment);
            if (appointment.getStatus() == null) {
                appointment.setStatus(0);
            }
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateAppointment(Appointment appointment) {
        Map<String, Object> response = new HashMap<>();
        Appointment existing = appointment.getId() == null ? null : appointmentRepository.findById(appointment.getId()).orElse(null);
        if (existing == null) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null
                || !appointment.getPatient().getId().equals(existing.getPatient().getId())) {
            response.put("message", "Patient does not own this appointment");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        hydrateRelations(appointment);
        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> cancelAppointment(Long id, String patientEmail) {
        Map<String, Object> response = new HashMap<>();
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (appointment.getPatient() == null || !appointment.getPatient().getEmail().equals(patientEmail)) {
            response.put("message", "Patient does not own this appointment");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        appointmentRepository.delete(appointment);
        response.put("message", "Appointment cancelled successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getAppointments(String date, String patientName, Long doctorId) {
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        if (patientName == null || patientName.isBlank() || "null".equalsIgnoreCase(patientName)) {
            response.put("appointments", patientService.toDTOs(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay().minusNanos(1))));
        } else {
            response.put("appointments", patientService.toDTOs(appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay().minusNanos(1))));
        }
        return ResponseEntity.ok(response);
    }

    @Transactional
    public void changeStatus(long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }

    private void hydrateRelations(Appointment appointment) {
        Long doctorId = appointment.getDoctor() == null ? null : appointment.getDoctor().getId();
        Long patientId = appointment.getPatient() == null ? null : appointment.getPatient().getId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
            appointment.setDoctor(doctor);
        }
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId).orElseThrow();
            appointment.setPatient(patient);
        }
    }

}
