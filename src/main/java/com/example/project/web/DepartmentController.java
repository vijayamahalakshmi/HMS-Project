package com.example.project.web;

import com.example.project.entity.Department;
import com.example.project.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
  private final DepartmentRepository repo;
  public DepartmentController(DepartmentRepository repo) { this.repo = repo; }

  @GetMapping
  public List<Department> all() { return repo.findAll(); }
}
