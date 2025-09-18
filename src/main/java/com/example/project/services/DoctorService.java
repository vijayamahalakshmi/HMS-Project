package com.example.project.services;

import com.example.project.repository.AppointmentRepository;
import com.example.project.repository.DepartmentRepository;
import com.example.project.repository.DoctorRepo;
import com.example.project.repository.DoctorRepository;
import com.example.project.repository.PatientRepository;
import com.example.project.repository.PrescriptionRepository;
import com.example.project.repository.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

import com.example.project.dto.AppointmentDTO;
import com.example.project.dto.AppointmentSummaryDTO;
import com.example.project.dto.DoctorSummaryDTO;
import com.example.project.dto.PatientSummaryDTO;
import com.example.project.dto.PrescriptionSummaryDTO;
import com.example.project.entity.Appointment;
import com.example.project.entity.Department;
import com.example.project.entity.Doctor;
import com.example.project.entity.Patient;
import com.example.project.entity.Prescription;
import com.example.project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService implements DoctorRepo{
  
  @Autowired
  private DoctorRepository repo;

  @Autowired DepartmentRepository deptrepo;

  @Autowired UserRepository userrepo;

  @Autowired PatientRepository patientRepo;

  @Autowired AppointmentRepository appointmentRepo;

  @Autowired PrescriptionRepository prescriptionRepo;

  @Override
  public ResponseEntity<Department> getDoctorDepartment(int doctorId)
  {
    return repo.findById(doctorId)
                .map(doctor -> ResponseEntity.ok(doctor.getDepartment()))
                .orElse(ResponseEntity.notFound().build());
    
  }


  @Override
  public Doctor addDoctor(int userId,int deptId,Doctor doctor)
  {
       Department dept = deptrepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User u=userrepo.findById(userId)
        .orElseThrow(() -> new RuntimeException("user not found"));

        doctor.setDepartment(dept);
        doctor.setUser(u);
        return repo.save(doctor);
  }

  public List<Map<String, Object>> getAllDoctorsSummary() {
    List<Object[]> results = repo.findAllDoctorsSummary();

    return results.stream().map(row -> {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[0]);
        map.put("name", row[1]);
        map.put("departmentName", row[2]);
        map.put("phone", row[3]);
        map.put("yearsExperience", row[4]);
        return map;
    }).collect(Collectors.toList());
}

@Override
public DoctorSummaryDTO updateDoctor(int doctorId, Doctor updatedDoctor) {
    return repo.findById(doctorId)
        .map(doctor -> {
            if (updatedDoctor.getName() != null) doctor.setName(updatedDoctor.getName());
            if (updatedDoctor.getPhone() != null) doctor.setPhone(updatedDoctor.getPhone());
            if (updatedDoctor.getYearsExperience() != null) doctor.setYearsExperience(updatedDoctor.getYearsExperience());
            if (updatedDoctor.getDepartment() != null) {
                Department dept = deptrepo.findById(updatedDoctor.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
                doctor.setDepartment(dept);
            }
            Doctor saved = repo.save(doctor);
            return new DoctorSummaryDTO(saved.getId(), saved.getName(),saved.getPhone(), saved.getDepartment().getName(),saved.getYearsExperience());
        })
        .orElseThrow(() -> new RuntimeException("Doctor not found with id " + doctorId));
}

public void deleteDoctor(int doctorId) {
    if (!repo.existsById(doctorId)) {
        throw new RuntimeException("Doctor not found with id " + doctorId);
    }
    repo.deleteById(doctorId);
}

public PatientSummaryDTO getPatientAppointmentsForDoctor(int doctorId, int patientId) {
    Patient patient = patientRepo.findById(patientId)
            .orElseThrow(() -> new NoSuchElementException("Patient not found"));

    List<Appointment> appointments = appointmentRepo.findByDoctorIdAndPatientId(doctorId, patientId);

    List<AppointmentDTO> appointmentDTOs = appointments.stream()
            .map(appt -> {
                // fetch prescription(s) for this appointmentId
                Prescription prescription = prescriptionRepo.findByAppointmentId(appt.getId())
                        .orElse(null);

                PrescriptionSummaryDTO prescriptionDTO = null;
                if (prescription != null) {
                    prescriptionDTO = PrescriptionSummaryDTO.builder()
                            .id(prescription.getId())
                            .appointmentId(appt.getId())
                            .notes(prescription.getNotes())
                            .medicinesText(prescription.getMedicinesText())
                            .build();
                }

                return new AppointmentDTO(
                        appt.getId(),
                        appt.getStartTime(),
                        appt.getEndTime(),
                        appt.getStatus(),
                        prescriptionDTO
                       
                );
            })
            .toList();

    return new PatientSummaryDTO(
            patient.getId(),
            patient.getFullName(),
            patient.getEmail(),
            patient.getPhone(),
            patient.getDob() != null ? patient.getDob().toString() : null,
            patient.getAddress(),
            appointmentDTOs
    );
}



}