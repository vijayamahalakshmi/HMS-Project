// service/PrescriptionService.java
package com.example.project.services;

import com.example.project.dto.PrescriptionSummaryDTO;
import com.example.project.entity.Appointment;
import com.example.project.entity.Prescription;
import com.example.project.repository.AppointmentRepository;
import com.example.project.repository.PrescriptionRepository;
import com.example.project.repository.PrescriptionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.NoSuchElementException;

@Service
@Transactional
public class PrescriptionService implements PrescriptionRepo {

    private final PrescriptionRepository prescriptionRepo;
    private final AppointmentRepository appointmentRepo;

    public PrescriptionService(PrescriptionRepository prescriptionRepo, AppointmentRepository appointmentRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.appointmentRepo = appointmentRepo;
    }

    public PrescriptionSummaryDTO addPrescription(int appointmentId,PrescriptionSummaryDTO dto) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found"));

        // Prevent duplicate prescriptions
        if (prescriptionRepo.findByAppointmentId(appointmentId).isPresent()) {
            throw new IllegalStateException("Prescription already exists for this appointment");
        }

        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .notes(dto.getNotes())
                .medicinesText(dto.getMedicinesText())
                .build();

        Prescription saved = prescriptionRepo.save(prescription);
        saved.setId(saved.getId());
        return PrescriptionSummaryDTO.builder()
            .id(saved.getId())
            .appointmentId(saved.getAppointment().getId())   // set here
            .notes(saved.getNotes())
            .medicinesText(saved.getMedicinesText())
            .build();
    }

    public PrescriptionSummaryDTO updatePrescription(int id, PrescriptionSummaryDTO dto) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prescription not found"));

        if (dto.getNotes() != null) prescription.setNotes(dto.getNotes());
        if (dto.getMedicinesText() != null) prescription.setMedicinesText(dto.getMedicinesText());

        Prescription updated = prescriptionRepo.save(prescription);
        

        return PrescriptionSummaryDTO.builder()
        .id(updated.getId())
        .appointmentId(updated.getAppointment().getId())
        .notes(updated.getNotes())
        .medicinesText(updated.getMedicinesText()).build();
        
    }

    public void deletePrescription(int id) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prescription not found"));
        prescriptionRepo.delete(prescription);
    }

    public PrescriptionSummaryDTO getPrescriptionByAppointment(int appointmentId) {
        Prescription prescription = prescriptionRepo.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Prescription not found"));
        return PrescriptionSummaryDTO.builder()
        .id(prescription.getId())
        .appointmentId(prescription.getAppointment().getId())
        .notes(prescription.getNotes())
        .medicinesText(prescription.getMedicinesText()).build();
       
    }
}
