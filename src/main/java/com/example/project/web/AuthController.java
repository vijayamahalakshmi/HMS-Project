package com.example.project.web;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.RegisterRequest;
import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.security.AppUserPrincipal;
import com.example.project.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtService jwt;
  private final UserRepository users;
  private final RoleRepository roles;
  private final PasswordEncoder encoder;

  public AuthController(
      AuthenticationManager authManager,
      JwtService jwt,
      UserRepository users,
      RoleRepository roles,
      PasswordEncoder encoder) {
    this.authManager = authManager;
    this.jwt = jwt;
    this.users = users;
    this.roles = roles;
    this.encoder = encoder;
  }

  // ---------- LOGIN ----------
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
    String id = req.getUsernameOrEmail().trim();
    String principal = id;

    // if the user typed an email, resolve to username for DaoAuthenticationProvider
    if (id.contains("@")) {
      principal = users.findByEmail(id.toLowerCase()).map(User::getUsername).orElse(null);
      if (!StringUtils.hasText(principal)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
      }
    }

    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(principal, req.getPassword())
    );

    AppUserPrincipal p = (AppUserPrincipal) auth.getPrincipal();
    String token = jwt.generateToken(p.getUser());

    return ResponseEntity.ok(Map.of(
        "token", token,
        "username", p.getUsername(),
        "role", p.getUser().getRole().getName()
    ));
  }

  // ---------- REGISTER ----------
  /*@PostMapping("/register")
  //@PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

    String username = req.getUsername().trim();
    String email = req.getEmail().trim().toLowerCase();

    if (users.existsByUsername(username)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "Username already taken"));
    }
    if (users.existsByEmail(email)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "Email already registered"));
    }

    // Extra password hardening: don't allow password to contain username or email local-part
    String pw = req.getPassword();
    String local = email.substring(0, email.indexOf('@'));
    String pwLower = pw.toLowerCase();
    if (pwLower.contains(username.toLowerCase()) || pwLower.contains(local.toLowerCase())) {
      return ResponseEntity.badRequest().body(
          Map.of("error", "Password must not contain your username or email local-part"));
    }

    Role role = roles.findByName(req.getRole().toUpperCase())
        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + req.getRole()));

    User u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setPasswordHash(encoder.encode(pw));
    u.setRole(role);

    User saved = users.save(u);

    String token = jwt.generateToken(saved);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "id", saved.getId(),
        "username", saved.getUsername(),
        "email", saved.getEmail(),
        "role", saved.getRole().getName(),
        "token", token
    ));
  }*/

 @PostMapping("/register")
public ResponseEntity<?> registerPatient(@Valid @RequestBody RegisterRequest req) {

    String username = req.getUsername().trim();
    String email = req.getEmail().trim().toLowerCase();

    if (users.existsByUsername(username)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Username already taken"));
    }
    if (users.existsByEmail(email)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Email already registered"));
    }

    // password hardening check
    String pw = req.getPassword();
    String local = email.substring(0, email.indexOf('@'));
    String pwLower = pw.toLowerCase();
    if (pwLower.contains(username.toLowerCase()) || pwLower.contains(local.toLowerCase())) {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Password must not contain your username or email local-part"));
    }

    Role patientRole = roles.findByName("PATIENT")
        .orElseThrow(() -> new IllegalArgumentException("Role PATIENT not found"));

    User u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setPasswordHash(encoder.encode(pw));
    u.setRole(patientRole);

    User saved = users.save(u);
    String token = jwt.generateToken(saved);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "id", saved.getId(),
        "username", saved.getUsername(),
        "email", saved.getEmail(),
        "role", saved.getRole().getName(),
        "token", token
    ));
}
@PostMapping("/admin/register")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> registerByAdmin(@Valid @RequestBody RegisterRequest req) {

    String username = req.getUsername().trim();
    String email = req.getEmail().trim().toLowerCase();

    if (users.existsByUsername(username)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Username already taken"));
    }
    if (users.existsByEmail(email)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Email already registered"));
    }

    String pw = req.getPassword();
    String local = email.substring(0, email.indexOf('@'));
    String pwLower = pw.toLowerCase();
    if (pwLower.contains(username.toLowerCase()) || pwLower.contains(local.toLowerCase())) {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Password must not contain your username or email local-part"));
    }

    String requestedRole = req.getRole().toUpperCase();
    if (!requestedRole.equals("ADMIN") && !requestedRole.equals("DOCTOR") && !requestedRole.equals("PATIENT")) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid role: " + requestedRole));
    }

    Role role = roles.findByName(requestedRole)
        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + requestedRole));

    User u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setPasswordHash(encoder.encode(pw));
    u.setRole(role);

    User saved = users.save(u);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "id", saved.getId(),
        "username", saved.getUsername(),
        "email", saved.getEmail(),
        "role", saved.getRole().getName()
    ));
}
}