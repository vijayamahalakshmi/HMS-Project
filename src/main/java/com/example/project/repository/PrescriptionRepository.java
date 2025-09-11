package com.example.project.repository;

import com.example.project.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
  Optional<Prescription> findByAppointmentId(Integer appointmentId);
}
