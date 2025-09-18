package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.*;

@Data
@AllArgsConstructor
public class PatientSummaryDTO {
    
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String dob;
    private String address;
    private List<AppointmentDTO> appointments;

     public PatientSummaryDTO(int id, String fullName, String email, String phone, String dob, String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
    }
}
