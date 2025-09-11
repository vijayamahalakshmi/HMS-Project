package com.example.project.web;

import com.example.project.entity.Appointment;
import com.example.project.repository.AppointmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private final AppointmentRepository repo;
  public AppointmentController(AppointmentRepository repo) { this.repo = repo; }

  @GetMapping("/doctor/{doctorId}")
  public List<Appointment> byDoctor(@PathVariable Integer doctorId) {
    return repo.findByDoctorId(doctorId);
  }

  @GetMapping("/patient/{patientId}")
  public List<Appointment> byPatient(@PathVariable Integer patientId) {
    return repo.findByPatientId(patientId);
  }
}
