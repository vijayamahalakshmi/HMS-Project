package com.example.project.repository;

import com.example.project.entity.Appointment;
import com.example.project.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query(value = "SELECT d.id, d.name, dept.name AS departmentName, d.phone, d.years_experience " +
                   "FROM doctor d " +
                   "JOIN department dept ON d.department_id = dept.id",
           nativeQuery = true)
    List<Object[]> findAllDoctorsSummary();
    List<Doctor> findByDepartmentId(Integer departmentId);
    
}
