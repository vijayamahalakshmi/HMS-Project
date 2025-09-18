package com.example.project.dto;

import java.time.LocalDateTime;

import com.example.project.entity.Appointment;
import com.example.project.entity.Prescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // Required by Jackson
@AllArgsConstructor 
public class AppointmentDTO {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime createdAt;
    private String doctorName;
    private String patientName;
    private PrescriptionSummaryDTO prescription;


    public AppointmentDTO(int id,LocalDateTime startTime, LocalDateTime endTime,String status ,PrescriptionSummaryDTO prescription){
                 this.id = id;
                 this.startTime = startTime;
                 this.endTime = endTime;
                 this.status =status;
                this.prescription = prescription;
               
    }
         

    public AppointmentDTO(Appointment appointment) {
    this.id = appointment.getId();
    this.startTime = appointment.getStartTime();
    this.endTime = appointment.getEndTime();
    this.status = appointment.getStatus();
    this.createdAt = appointment.getCreatedAt();
    this.doctorName = appointment.getDoctor().getName();
    this.patientName = appointment.getPatient().getFullName();
}


   /* *public AppointmentDTO(int id2, LocalDateTime startTime2, LocalDateTime endTime2, String status2, String name,
            String fullName) {
        //TODO Auto-generated constructor stub

        this.id=id2;
        this.startTime=startTime2;
        this.endTime=endTime2;
        this.status=status2;
        this.doctorName=name;
        this.patientName=fullName;
    }*/
    
}
