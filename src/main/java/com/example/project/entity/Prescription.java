package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity @Table(name = "PRESCRIPTION")
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class Prescription {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @OneToOne(optional = false)
  @JoinColumn(name = "APPOINTMENT_ID", unique = true)
  private Appointment appointment;

  @Lob @Column(name = "NOTES")
  private String notes;

  @Lob @Column(name = "MEDICINES_TEXT")
  private String medicinesText;

  @CreationTimestamp
  @Column(name = "CREATED_AT", nullable = false,updatable = false)
  private LocalDateTime createdAt;


}
