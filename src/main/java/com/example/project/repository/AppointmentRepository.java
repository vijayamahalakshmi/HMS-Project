package com.example.project.repository;

import com.example.project.dto.AppointmentSummaryDTO;
import com.example.project.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
  @Query("SELECT new com.example.project.dto.AppointmentSummaryDTO(a.id, a.startTime, a.endTime, a.status, " +
           "p.id, p.fullName, p.email, p.phone) " +
           "FROM Appointment a JOIN a.patient p " +
           "WHERE a.doctor.id = :doctorId")
    List<AppointmentSummaryDTO> findAppointmentsByDoctorId(@Param("doctorId") int doctorId);

  List<Appointment> findByDoctorId(Integer doctorId);
  List<Appointment> findByPatientId(Integer patientId);

  List<Appointment> findByDoctorIdAndPatientId(int doctorId, int patientId);
  List<Appointment> findByDoctorIdAndStartTimeBetween(Integer doctorId, LocalDateTime start, LocalDateTime end);
}
