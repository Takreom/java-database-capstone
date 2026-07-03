package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select a from Appointment a left join fetch a.doctor d left join fetch a.patient p where d.id = :doctorId and a.appointmentTime between :start and :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(@Param("doctorId") Long doctorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select a from Appointment a left join fetch a.doctor d left join fetch a.patient p where d.id = :doctorId and lower(p.name) like lower(concat('%', :patientName, '%')) and a.appointmentTime between :start and :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(@Param("doctorId") Long doctorId, @Param("patientName") String patientName, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    @Query("select a from Appointment a left join fetch a.doctor d left join fetch a.patient p where lower(d.name) like lower(concat('%', :doctorName, '%')) and p.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(@Param("doctorName") String doctorName, @Param("patientId") Long patientId);

    @Query("select a from Appointment a left join fetch a.doctor d left join fetch a.patient p where lower(d.name) like lower(concat('%', :doctorName, '%')) and p.id = :patientId and a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(@Param("doctorName") String doctorName, @Param("patientId") Long patientId, @Param("status") int status);

    @Modifying
    @Transactional
    @Query("update Appointment a set a.status = :status where a.id = :id")
    void updateStatus(@Param("status") int status, @Param("id") long id);

}
