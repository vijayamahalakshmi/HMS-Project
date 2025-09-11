package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "ROLES")
@Data @NoArgsConstructor @AllArgsConstructor
public class Role {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME", unique = true, nullable = false)
  private String name; // ADMIN, DOCTOR, PATIENT
}
