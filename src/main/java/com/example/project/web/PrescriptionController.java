package com.example.project.web;

import com.example.project.dto.PrescriptionSummaryDTO;
import com.example.project.entity.Prescription;
import com.example.project.repository.PrescriptionRepository;
import com.example.project.services.PrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

  private final PrescriptionRepository repo;

  @Autowired
  private PrescriptionService service;
  public PrescriptionController(PrescriptionRepository repo) { this.repo = repo; }

  /*@GetMapping("/by-appointment/{appointmentId}")
 // @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
  public ResponseEntity<?> byAppointment(@PathVariable Integer appointmentId) {
    return repo.findByAppointmentId(appointmentId)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }*/

   @PostMapping("/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionSummaryDTO> add(@PathVariable("appointmentId") int appointmentId ,@RequestBody PrescriptionSummaryDTO dto) {
        return ResponseEntity.ok(service.addPrescription(appointmentId,dto));
    }

    @PutMapping("/{id}")
     @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionSummaryDTO> update(@PathVariable int id, @RequestBody PrescriptionSummaryDTO dto) {
        return ResponseEntity.ok(service.updatePrescription(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionSummaryDTO> getByAppointment(@PathVariable int appointmentId) {
        return ResponseEntity.ok(service.getPrescriptionByAppointment(appointmentId));
    }
}
