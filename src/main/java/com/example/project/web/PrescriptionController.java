package com.example.project.web;

import com.example.project.entity.Prescription;
import com.example.project.repository.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {
  private final PrescriptionRepository repo;
  public PrescriptionController(PrescriptionRepository repo) { this.repo = repo; }

  @GetMapping("/by-appointment/{appointmentId}")
  public ResponseEntity<?> byAppointment(@PathVariable Integer appointmentId) {
    return repo.findByAppointmentId(appointmentId)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
