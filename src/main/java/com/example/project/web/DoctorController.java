package com.example.project.web;

import com.example.project.dto.AppointmentSummaryDTO;
import com.example.project.dto.DoctorSummaryDTO;
import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Department;
import com.example.project.entity.Doctor;
import com.example.project.repository.DoctorRepository;
import com.example.project.services.AppointmentService;
import com.example.project.services.DoctorService;
import com.example.project.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Doctor APIs", description = "Endpoints for managing doctors")
public class DoctorController {

  private final DoctorRepository repo;
  
  @Autowired
  private PatientService patientService;

  @Autowired
  private AppointmentService appointmentService;

  public DoctorController(DoctorRepository repo) { this.repo = repo; }


  @Autowired
  public DoctorService doctorService;

  @GetMapping("/")
  //@Operation(summary = "G", description = "Fetch details of a doctor using their ID")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Map<String, Object>> all() { return doctorService.getAllDoctorsSummary(); }

  @GetMapping("/by-department/{deptId}")
  @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
  public List<Doctor> byDepartment(@PathVariable Integer deptId) {
    return repo.findByDepartmentId(deptId);
  }

  @GetMapping("/{doctorId}/department")
  @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<Department> getDoctorDepartment(@PathVariable int doctorId) {
      return doctorService.getDoctorDepartment(doctorId);
    }

  @PostMapping("/add/{deptId}/{userId}")
  @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
  public Doctor addDoctor(@PathVariable("deptId") int deptId,@PathVariable("userId") int userId, @RequestBody Doctor doctor){
    return doctorService.addDoctor(userId,deptId, doctor);
  }

  @PutMapping("/update/{doctId}")
  @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<DoctorSummaryDTO> updateDoctor(
            @PathVariable("doctId") Integer doctorId,
            @RequestBody Doctor updatedDoctor) {
        DoctorSummaryDTO doctor = doctorService.updateDoctor(doctorId, updatedDoctor);
        return ResponseEntity.ok(doctor);
    }

     @DeleteMapping("/delete/{doctId}")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("doctId") int doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    

    @GetMapping("/{doctorId}/patients")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<PatientSummaryDTO>> getPatientsByDoctor(@PathVariable int doctorId) {
        return ResponseEntity.ok(patientService.getPatientsByDoctorId(doctorId));
    }

    @GetMapping("/{doctorId}/appointments")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentSummaryDTO>> getDoctorAppointments(@PathVariable int doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    @GetMapping("/{doctorId}/patients/{patientId}/appointments")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientSummaryDTO> getPatientAppointments(
            @PathVariable int doctorId,
            @PathVariable int patientId) {
        return ResponseEntity.ok(doctorService.getPatientAppointmentsForDoctor(doctorId, patientId));
    }
}
