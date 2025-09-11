package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "DEPARTMENT")
@Data @NoArgsConstructor @AllArgsConstructor
public class Department {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;
}
