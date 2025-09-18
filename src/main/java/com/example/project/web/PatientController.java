package com.example.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Patient;
import com.example.project.services.PatientService;



@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

     @PostMapping("/add/{userId}")
    public ResponseEntity<PatientSummaryDTO> addPatientProfile(
            @PathVariable("userId") int userId,
            @RequestBody Patient patient) {
         System.out.println(">>> Received JSON: " + patient);
        PatientSummaryDTO savedPatient = patientService.addPatient(userId, patient);
        return ResponseEntity.ok(savedPatient);
    }

     @PostMapping("/update/{patientId}")
    public ResponseEntity<PatientSummaryDTO> updatePatientProfile(
            @PathVariable("patientId") int patientId,
            @RequestBody Patient patient) {
         //System.out.println(">>> Received JSON: " + patient);
        PatientSummaryDTO savedPatient = patientService.updatePatient(patientId, patient);
        return ResponseEntity.ok(savedPatient);
    }

    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable Integer patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok("Patient with id " + patientId + " deleted successfully.");
}

    
}
