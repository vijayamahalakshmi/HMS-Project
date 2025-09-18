package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorSummaryDTO {
    
    private int id;
    private String name;
    private String phone;
    private String departmentName;
    private int yearsExperience;
    
}
