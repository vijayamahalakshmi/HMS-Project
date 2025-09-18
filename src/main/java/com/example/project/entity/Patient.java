package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "PATIENT")
@Data @NoArgsConstructor @AllArgsConstructor
public class Patient {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @OneToOne
  @JoinColumn(name = "USER_ID", unique = true)
  private User user;

  @Column(name = "FULL_NAME", nullable = false)
  private String fullName;

  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "DOB")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private java.time.LocalDate dob;

  @Column(name = "ADDRESS")
  private String address;
}
