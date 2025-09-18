package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import com.example.project.dto.PrescriptionSummaryDTO;

@Data
@AllArgsConstructor
public class AppointmentSummaryDTO {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private int patientId;
    private String fullName;
    private String email;
    private String phone;
    private PrescriptionSummaryDTO prescription;
    public AppointmentSummaryDTO(int id,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             String status,
                             int patientId,
                             String fullName,
                             String email,
                             String phone) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.patientId = patientId;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.prescription = null; // will be set later if needed
}

}
