package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Service
public class Service {

    private static final Set<String> SUPPORTED_ROLES = Set.of("admin", "doctor", "patient");
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(AdminRepository adminRepository, PatientRepository patientRepository, TokenService tokenService,
                   DoctorService doctorService, PatientService patientService) {
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Map<String, String> validateToken(String token, String role) {
        Map<String, String> errors = new HashMap<>();

        if (token == null || token.isBlank()) {
            errors.put("token", "Token is required");
        }

        if (role == null || !SUPPORTED_ROLES.contains(role)) {
            errors.put("role", "Unsupported role");
        }

        if (errors.isEmpty() && !tokenService.validateToken(token, role)) {
            errors.put("message", "Invalid or expired token");
        }

        return errors;
    }

    public ResponseEntity<Map<String, Object>> validateAdmin(Admin admin) {
        Map<String, Object> response = new HashMap<>();
        Admin storedAdmin = adminRepository.findByUsername(admin.getUsername());

        if (storedAdmin == null || !storedAdmin.getPassword().equals(admin.getPassword())) {
            response.put("message", "Invalid admin credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "Login successful");
        response.put("token", tokenService.generateToken(storedAdmin.getUsername()));
        return ResponseEntity.ok(response);
    }

    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    public ResponseEntity<Map<String, Object>> validatePatientLogin(Login login) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getIdentifier());

        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid patient credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "Login successful");
        response.put("token", tokenService.generateToken(patient.getEmail()));
        response.put("patient", patient);
        return ResponseEntity.ok(response);
    }

    public Map<String, String> validateAppointment(Appointment appointment) {
        Map<String, String> errors = new HashMap<>();

        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            errors.put("doctor", "Doctor is required");
        }
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            errors.put("patient", "Patient is required");
        }
        if (appointment.getAppointmentTime() == null) {
            errors.put("appointmentTime", "Appointment time is required");
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        if (!doctorService.isDoctorAvailable(appointment.getDoctor().getId(), appointment.getAppointmentTime())) {
            errors.put("message", "Doctor is not available at the selected time");
        }

        return errors;
    }

    public ResponseEntity<Map<String, Object>> filterPatientAppointment(String condition, String name, String token) {
        Map<String, String> validation = validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(validation));
        }

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (isNullFilter(name) && isNullFilter(condition)) {
            return patientService.getPatientAppointment(patient.getId(), token);
        }
        if (isNullFilter(name)) {
            return patientService.filterByCondition(condition, patient.getId());
        }
        if (isNullFilter(condition) || "all".equalsIgnoreCase(condition)) {
            return patientService.filterByDoctor(name, patient.getId());
        }
        return patientService.filterByDoctorAndCondition(condition, name, patient.getId());
    }

    public boolean isNullFilter(String value) {
        return value == null || value.isBlank() || "null".equalsIgnoreCase(value) || "all".equalsIgnoreCase(value);
    }
}
