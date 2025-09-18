package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "DOCTOR")
@Data @NoArgsConstructor @AllArgsConstructor
public class Doctor {
  
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  // Optional link to USERS
  @OneToOne
  @JoinColumn(name = "USER_ID", unique = true)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "DEPARTMENT_ID")
  private Department department;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "YEARS_EXPERIENCE")
  private Integer yearsExperience;
}
