package com.example.project.repository;

import com.example.project.dto.PrescriptionSummaryDTO;

public interface PrescriptionRepo {
    PrescriptionSummaryDTO addPrescription(int appointmentId,PrescriptionSummaryDTO dto);
    PrescriptionSummaryDTO updatePrescription(int id,PrescriptionSummaryDTO dto);
    void deletePrescription(int id);
    PrescriptionSummaryDTO getPrescriptionByAppointment(int appointmentId);
    
    
}
