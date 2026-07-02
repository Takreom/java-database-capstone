package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "name no puede ser nulo")
    @Size(min = 3, max = 100, message = "name debe tener entre 3 y 100 caracteres")
    private String name;

    @NotNull(message = "specialty no puede ser nulo")
    @Size(min = 3, max = 50, message = "specialty debe tener entre 3 y 50 caracteres")
    private String specialty;

    @NotNull(message = "email no puede ser nulo")
    @Email(message = "email debe tener un formato valido")
    private String email;

    @NotNull(message = "password no puede ser nulo")
    @Size(min = 6, message = "password debe tener al menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "phone no puede ser nulo")
    @Pattern(regexp = "\\d{10}", message = "phone debe tener 10 digitos")
    private String phone;

    @ElementCollection
    private List<String> availableTimes;

    public Doctor() {
    }

    public Doctor(String name, String specialty, String email, String password, String phone, List<String> availableTimes) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.availableTimes = availableTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
