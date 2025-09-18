package com.example.project.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Patient;
import com.example.project.entity.User;
import com.example.project.repository.PatientRepo;
import com.example.project.repository.PatientRepository;
import com.example.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class PatientService implements PatientRepo{

    @Autowired
    private PatientRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<PatientSummaryDTO> getPatientsByDoctorId(int doctorId){
        return repo.findPatientsByDoctorId(doctorId);
    }

    @Override
    public PatientSummaryDTO addPatient(int userId, Patient patient) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Attach user to patient
        patient.setUser(user);
        Patient saved = repo.save(patient);

        return new PatientSummaryDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getDob() == null ? null : saved.getDob().toString(),
                saved.getAddress()
        );
    }

    @Override
    public PatientSummaryDTO updatePatient(int patientId, Patient patientDetails) {
        Patient existingPatient = repo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        if (patientDetails.getFullName() != null) existingPatient.setFullName(patientDetails.getFullName());
        if (patientDetails.getEmail() != null)     existingPatient.setEmail(patientDetails.getEmail());
        if (patientDetails.getPhone() != null)     existingPatient.setPhone(patientDetails.getPhone());
        if (patientDetails.getDob() != null)       existingPatient.setDob(patientDetails.getDob());
        if (patientDetails.getAddress() != null)   existingPatient.setAddress(patientDetails.getAddress());

        Patient saved = repo.save(existingPatient);
        return new PatientSummaryDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getDob() == null ? null : saved.getDob().toString(),
                saved.getAddress()
        );
    }

    @Override
    public void deletePatient(int patientId) {
        Patient existingPatient = repo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        repo.delete(existingPatient);
    }

    // ✅ NEW: latest patients (by ID DESC), default size 10
    public List<PatientSummaryDTO> getLatestPatients(int size) {
        if (size <= 0) size = 10;
        PageRequest pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        return repo.findAllByOrderByIdDesc(pageable)
                .getContent()
                .stream()
                .map(p -> new PatientSummaryDTO(
                        p.getId(),
                        p.getFullName(),
                        p.getEmail(),
                        p.getPhone(),
                        p.getDob() == null ? null : p.getDob().toString(),
                        p.getAddress()
                ))
                .collect(Collectors.toList());
    }
}
