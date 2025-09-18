package com.example.project.repository;

import com.example.project.dto.DoctorSummaryDTO;
import com.example.project.dto.PatientSummaryDTO;
import com.example.project.entity.Department;
import com.example.project.entity.Doctor;

import org.springframework.http.ResponseEntity;

public interface DoctorRepo {
    ResponseEntity<Department> getDoctorDepartment(int doctorId);
    Doctor addDoctor(int userId,int deptId,Doctor doctor);
    DoctorSummaryDTO updateDoctor(int doctorId,Doctor updatedDoctor);
    void deleteDoctor(int doctorId);
    PatientSummaryDTO getPatientAppointmentsForDoctor(int doctorId, int patientId);
}
