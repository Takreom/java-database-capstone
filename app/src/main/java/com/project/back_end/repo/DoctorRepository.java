package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmail(String email);

    @Query("select d from Doctor d where lower(d.name) like lower(concat('%', :name, '%'))")
    List<Doctor> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("select d from Doctor d where lower(d.name) like lower(concat('%', :name, '%')) and lower(d.specialty) = lower(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(@Param("name") String name, @Param("specialty") String specialty);

    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}
