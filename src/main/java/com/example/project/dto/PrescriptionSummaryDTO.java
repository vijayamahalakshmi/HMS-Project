package com.example.project.dto;
import com.example.project.entity.Prescription;
import com.example.project.entity.Prescription.PrescriptionBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionSummaryDTO {
    private int id;             // optional (for update/delete)
    private int appointmentId;  // required
    private String notes;
    private String medicinesText;

    public PrescriptionSummaryDTO(Prescription prescription) {
        this.id = prescription.getId();
        this.appointmentId = prescription.getAppointment().getId();
        this.notes = prescription.getNotes();
        this.medicinesText = prescription.getMedicinesText();
    }

    public static PrescriptionSummaryDTO fromEntity(Prescription prescription) {
        return PrescriptionSummaryDTO.builder()
                .id(prescription.getId())
                .appointmentId(prescription.getAppointment().getId())
                .notes(prescription.getNotes())
                .medicinesText(prescription.getMedicinesText())
                .build();
    }


    
}
