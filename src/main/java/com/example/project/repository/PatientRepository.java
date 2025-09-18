package com.example.project.repository;

import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("SELECT new com.example.project.dto.PatientSummaryDTO(p.id, p.fullName, p.email, p.phone, CAST(p.dob AS string), p.address) " +
           "FROM Appointment a JOIN a.patient p " +
           "WHERE a.doctor.id = :doctorId")
    List<PatientSummaryDTO> findPatientsByDoctorId(@Param("doctorId") int doctorId);

    
}
