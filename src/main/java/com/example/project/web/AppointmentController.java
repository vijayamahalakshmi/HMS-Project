package com.example.project.web;

import com.example.project.dto.AppointmentDTO;
import com.example.project.entity.Appointment;
import com.example.project.repository.AppointmentRepository;
import com.example.project.services.AppointmentService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private final AppointmentRepository repo;

  @Autowired
  private AppointmentService appointmentService;

  public AppointmentController(AppointmentRepository repo) { this.repo = repo; }

  @GetMapping("/doctor/{doctorId}")
  @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
  public List<Appointment> byDoctor(@PathVariable Integer doctorId) {
    return repo.findByDoctorId(doctorId);
  }

  @GetMapping("/patient/{patientId}")
  @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
  public List<Appointment> byPatient(@PathVariable Integer patientId) {
    return repo.findByPatientId(patientId);
  }

  @PostMapping("/book/{doctorId}/{patientId}")
    public ResponseEntity<AppointmentDTO> bookAppointment(
            @PathVariable Integer doctorId,
            @PathVariable Integer patientId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        AppointmentDTO appointment = appointmentService.bookAppointment(doctorId, patientId, start, end);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable int id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String status) {

        AppointmentDTO updated = appointmentService.updateAppointment(id, start, end, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/cancel")
    public void cancelAppointment(@PathVariable int id) {
        appointmentService.cancelAppointment(id);
    }
  @GetMapping("/latest")
@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT')")
public ResponseEntity<List<AppointmentDTO>> latestAppointments(
        @RequestParam(defaultValue = "10") int size) {
    List<AppointmentDTO> results = appointmentService.getLatestAppointments(size);
    return ResponseEntity.ok(results);
}


}
