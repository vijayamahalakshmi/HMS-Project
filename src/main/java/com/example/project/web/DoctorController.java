package com.example.project.web;

import com.example.project.entity.Doctor;
import com.example.project.repository.DoctorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
  private final DoctorRepository repo;
  public DoctorController(DoctorRepository repo) { this.repo = repo; }

  @GetMapping
  public List<Doctor> all() { return repo.findAll(); }

  @GetMapping("/by-department/{deptId}")
  public List<Doctor> byDepartment(@PathVariable Integer deptId) {
    return repo.findByDepartmentId(deptId);
  }
}
