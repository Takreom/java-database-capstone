package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!patient.getEmail().equals(tokenService.extractEmail(token))) {
            response.put("message", "Patient token does not match requested patient");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("appointments", toDTOs(appointmentRepository.findByPatientId(id)));
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointmentForDoctor(Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("appointments", toDTOs(appointmentRepository.findByPatientId(id)));
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Integer status = statusFromCondition(condition);
        Map<String, Object> response = new HashMap<>();
        if (status == null) {
            response.put("message", "Invalid condition");
            response.put("appointments", List.of());
            return ResponseEntity.badRequest().body(response);
        }
        response.put("appointments", toDTOs(appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status)));
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        response.put("appointments", toDTOs(appointmentRepository.filterByDoctorNameAndPatientId(name, patientId)));
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Integer status = statusFromCondition(condition);
        Map<String, Object> response = new HashMap<>();
        if (status == null) {
            response.put("message", "Invalid condition");
            response.put("appointments", List.of());
            return ResponseEntity.badRequest().body(response);
        }
        response.put("appointments", toDTOs(appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status)));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(tokenService.extractEmail(token));
        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("patient", patient);
        return ResponseEntity.ok(response);
    }

    public List<AppointmentDTO> toDTOs(List<Appointment> appointments) {
        return appointments.stream().map(this::toDTO).toList();
    }

    public AppointmentDTO toDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor() == null ? null : appointment.getDoctor().getId(),
                appointment.getDoctor() == null ? null : appointment.getDoctor().getName(),
                appointment.getPatient() == null ? null : appointment.getPatient().getId(),
                appointment.getPatient() == null ? null : appointment.getPatient().getName(),
                appointment.getPatient() == null ? null : appointment.getPatient().getEmail(),
                appointment.getPatient() == null ? null : appointment.getPatient().getPhone(),
                appointment.getPatient() == null ? null : appointment.getPatient().getAddress(),
                appointment.getAppointmentTime(),
                appointment.getStatus() == null ? 0 : appointment.getStatus());
    }

    private Integer statusFromCondition(String condition) {
        if ("future".equalsIgnoreCase(condition) || "upcoming".equalsIgnoreCase(condition)) {
            return 0;
        }
        if ("past".equalsIgnoreCase(condition) || "completed".equalsIgnoreCase(condition)) {
            return 1;
        }
        return null;
    }
}
