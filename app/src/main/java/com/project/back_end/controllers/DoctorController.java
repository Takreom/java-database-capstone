package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
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
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable String user, @PathVariable Long doctorId,
                                                                      @PathVariable String date, @PathVariable String token) {
        Map<String, String> errors = service.validateToken(token, user);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }
        return doctorService.getDoctorAvailability(doctorId, date);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return doctorService.getDoctors();
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = service.validateToken(token, "admin");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }

        int result = doctorService.saveDoctor(doctor);
        if (result == -1) {
            response.put("message", "Doctor already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (result == 0) {
            response.put("message", "Unable to save doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("message", "Doctor saved successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = service.validateToken(token, "admin");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }

        int result = doctorService.updateDoctor(doctor);
        if (result == -1) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (result == 0) {
            response.put("message", "Unable to update doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("message", "Doctor updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = service.validateToken(token, "admin");
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(errors));
        }

        int result = doctorService.deleteDoctor(id);
        if (result == -1) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (result == 0) {
            response.put("message", "Unable to delete doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("message", "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(@PathVariable String name, @PathVariable String time, @PathVariable String speciality) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, time, speciality);
    }

}
