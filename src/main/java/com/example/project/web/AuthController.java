package com.example.project.web;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.RegisterRequest;
import com.example.project.entity.Role;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  // -------- REGISTER --------
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    if (!StringUtils.hasText(req.getUsername()) ||
        !StringUtils.hasText(req.getEmail()) ||
        !StringUtils.hasText(req.getPassword()) ||
        !StringUtils.hasText(req.getRole())) {
      return ResponseEntity.badRequest().body(Map.of("error", "username, email, password, role are required"));
    }

    if (users.existsByUsername(req.getUsername())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "username already used"));
    }
    if (users.existsByEmail(req.getEmail())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "email already used"));
    }

    Role role = roles.findByName(req.getRole().toUpperCase())
        .orElseGet(() -> roles.save(new Role(null, req.getRole().toUpperCase())));

    User u = new User();
    u.setUsername(req.getUsername().trim());
    u.setEmail(req.getEmail().trim());
    u.setPasswordHash(encoder.encode(req.getPassword()));
    u.setRole(role);
    users.save(u);

    String token = jwt.generateToken(u);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "token", token,
        "username", u.getUsername(),
        "role", role.getName()
    ));
  }

  // -------- LOGIN --------
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    if (!StringUtils.hasText(req.getUsernameOrEmail()) || !StringUtils.hasText(req.getPassword())) {
      return ResponseEntity.badRequest().body(Map.of("error", "usernameOrEmail and password are required"));
    }

    // DaoAuthenticationProvider expects a username.
    String id = req.getUsernameOrEmail().trim();
    String principal = id;

    // If an email was sent, resolve it to the username first
    if (id.contains("@")) {
      principal = users.findByEmail(id).map(User::getUsername).orElse(null);
      if (!StringUtils.hasText(principal)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
      }
    }

    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(principal, req.getPassword())
    );

    // Load the entity to build the JWT
    User u = users.findByUsername(auth.getName()).orElseThrow();
    String token = jwt.generateToken(u);

    return ResponseEntity.ok(Map.of(
        "token", token,
        "username", u.getUsername(),
        "role", u.getRole().getName()
    ));
  }
}
