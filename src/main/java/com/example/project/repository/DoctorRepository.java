package com.example.project.repository;

import com.example.project.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
  List<Doctor> findByDepartmentId(Integer departmentId);
}
