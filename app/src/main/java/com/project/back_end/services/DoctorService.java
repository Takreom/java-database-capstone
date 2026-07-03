package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(Long doctorId, String date) {
        Map<String, Object> response = new HashMap<>();
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        LocalDate localDate = LocalDate.parse(date);
        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay().minusNanos(1));
        List<String> bookedTimes = bookedAppointments.stream()
                .map(appointment -> appointment.getAppointmentTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .toList();
        List<String> availableTimes = doctor.getAvailableTimes() == null ? List.of() : doctor.getAvailableTimes().stream()
                .filter(time -> !bookedTimes.contains(normalizeTime(time)))
                .toList();

        response.put("availableTimes", availableTimes);
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public boolean isDoctorAvailable(Long doctorId, LocalDateTime appointmentTime) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null || doctor.getAvailableTimes() == null) {
            return false;
        }

        String requestedTime = appointmentTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        boolean inSchedule = doctor.getAvailableTimes().stream().map(this::normalizeTime).anyMatch(requestedTime::equals);
        if (!inSchedule) {
            return false;
        }

        LocalDate date = appointmentTime.toLocalDate();
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay().minusNanos(1))
                .stream()
                .noneMatch(appointment -> appointment.getAppointmentTime().equals(appointmentTime));
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (doctor.getId() == null || !doctorRepository.existsById(doctor.getId())) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findAll();
        doctors.forEach(doctor -> {
            if (doctor.getAvailableTimes() != null) {
                doctor.getAvailableTimes().size();
            }
        });
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public int deleteDoctor(Long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                return -1;
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> validateDoctor(Login login) {
        Map<String, Object> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid doctor credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        if (doctor.getAvailableTimes() != null) {
            doctor.getAvailableTimes().size();
        }

        response.put("message", "Login successful");
        response.put("token", tokenService.generateToken(doctor.getEmail()));
        response.put("doctor", doctor);
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterDoctorsByNameSpecilityandTime(String name, String time, String speciality) {
        List<Doctor> doctors;

        boolean hasName = !isNullFilter(name);
        boolean hasTime = !isNullFilter(time);
        boolean hasSpeciality = !isNullFilter(speciality);

        if (hasName && hasSpeciality) {
            doctors = filterDoctorByNameAndSpecility(name, speciality);
        } else if (hasName) {
            doctors = findDoctorByName(name);
        } else if (hasSpeciality) {
            doctors = filterDoctorBySpecility(speciality);
        } else {
            doctors = doctorRepository.findAll();
        }

        if (hasTime) {
            doctors = filterDoctorByTime(doctors, time);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes() != null && doctor.getAvailableTimes().stream().anyMatch(slot -> matchesTime(slot, time)))
                .toList();
    }

    public List<Doctor> filterDoctorByNameAndSpecility(String name, String speciality) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, speciality);
    }

    public List<Doctor> filterDoctorBySpecility(String speciality) {
        return doctorRepository.findBySpecialtyIgnoreCase(speciality);
    }

    private boolean matchesTime(String slot, String time) {
        if ("AM".equalsIgnoreCase(time)) {
            return parseHour(slot) < 12;
        }
        if ("PM".equalsIgnoreCase(time)) {
            return parseHour(slot) >= 12;
        }
        return normalizeTime(slot).equals(normalizeTime(time));
    }

    private int parseHour(String value) {
        return LocalTime.parse(normalizeTime(value)).getHour();
    }

    private String normalizeTime(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String normalized = value.trim();
        if (normalized.contains("-")) {
            normalized = normalized.substring(0, normalized.indexOf('-')).trim();
        }
        if (normalized.length() == 4) {
            normalized = "0" + normalized;
        }
        return normalized;
    }

    private boolean isNullFilter(String value) {
        return value == null || value.isBlank() || "null".equalsIgnoreCase(value) || "all".equalsIgnoreCase(value);
    }

}
