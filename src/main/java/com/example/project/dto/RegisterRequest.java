package com.example.project.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 30, message = "Username must be 3–30 characters")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9._-]{2,29}$",
      message = "Username must start with a letter and can contain letters, digits, ., _ or -"
  )
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
      message = "Email must be a @gmail.com address"
  )
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 64, message = "Password must be 8–64 characters")
  @Pattern(
      // at least 1 lower, 1 upper, 1 digit, 1 special
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[()\\[\\]{}@$!%*?&#^_+\\-=.]).{8,64}$",
      message = "Password must contain upper, lower, digit, and special character"
  )
  private String password;

  @NotBlank(message = "Role is required")
  @Pattern(
      regexp = "ADMIN|DOCTOR|PATIENT",
      flags = Pattern.Flag.CASE_INSENSITIVE,
      message = "Role must be ADMIN, DOCTOR, or PATIENT"
  )
  private String role;

  // getters/setters
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
}
