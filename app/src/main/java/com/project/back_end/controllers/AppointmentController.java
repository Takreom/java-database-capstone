package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final Service service;
    private final TokenService tokenService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentService appointmentService, Service service, TokenService tokenService,
                                 DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.service = service;
        this.tokenService = tokenService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(@PathVariable String date, @PathVariable String patientName,
                                                                @PathVariable String token) {
        Map<String, String> errors = service.validateToken(token, "doctor");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }
        Doctor doctor = doctorRepository.findByEmail(tokenService.extractEmail(token));
        if (doctor == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return appointmentService.getAppointments(date, patientName, doctor.getId());
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(@Valid @RequestBody Appointment appointment, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> tokenErrors = service.validateToken(token, "patient");
        if (!tokenErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(tokenErrors));
        }

        Map<String, String> appointmentErrors = service.validateAppointment(appointment);
        if (!appointmentErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(new HashMap<>(appointmentErrors));
        }

        if (appointmentService.bookAppointment(appointment) == 0) {
            response.put("message", "Unable to book appointment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("message", "Appointment booked successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(@Valid @RequestBody Appointment appointment, @PathVariable String token) {
        Map<String, String> errors = service.validateToken(token, "patient");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }
        Map<String, String> appointmentErrors = service.validateAppointment(appointment);
        if (!appointmentErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(new HashMap<>(appointmentErrors));
        }
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        Map<String, String> errors = service.validateToken(token, "patient");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }
        return appointmentService.cancelAppointment(id, tokenService.extractEmail(token));
    }

}
