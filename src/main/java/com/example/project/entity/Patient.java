package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "PATIENT")
@Data @NoArgsConstructor @AllArgsConstructor
public class Patient {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

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
  private java.sql.Date dob;

  @Column(name = "ADDRESS")
  private String address;
}
