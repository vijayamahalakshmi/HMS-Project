package com.example.project.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MeController {
  @GetMapping("/me")
  public Map<String, Object> me(Authentication auth) {
    return Map.of("username", auth.getName());
  }
}
