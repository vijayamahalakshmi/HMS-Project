package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "USERS")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "USERNAME", nullable = false, unique = true)
  private String username;

  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "PASSWORD_HASH", nullable = false)
  private String passwordHash;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ROLE_ID", nullable = false)
  private Role role;
}
