package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "APPOINTMENT")
@Data @NoArgsConstructor @AllArgsConstructor
public class Appointment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne(optional = false) @JoinColumn(name = "DOCTOR_ID")
  private Doctor doctor;

  @ManyToOne(optional = false) @JoinColumn(name = "PATIENT_ID")
  private Patient patient;

  @Column(name = "START_TIME", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "END_TIME", nullable = false)
  private LocalDateTime endTime;

  @Column(name = "STATUS", nullable = false)
  private String status; // PENDING/BOOKED/CANCELLED/CONFIRMED/NO_SHOW

  @Column(name = "CREATED_AT", nullable = false)
  private LocalDateTime createdAt;
}
