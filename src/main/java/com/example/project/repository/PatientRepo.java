package com.example.project.repository;

import java.util.*;
import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Patient;

public interface PatientRepo{
    List<PatientSummaryDTO> getPatientsByDoctorId(int doctorId);
    PatientSummaryDTO addPatient(int userId,Patient patient);
    PatientSummaryDTO updatePatient(int patientId,Patient patient);
    void deletePatient(int patientId);
}