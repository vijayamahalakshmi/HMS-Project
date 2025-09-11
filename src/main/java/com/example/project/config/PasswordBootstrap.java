package com.example.project.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

@Configuration
public class PasswordBootstrap {

  @Bean
  CommandLineRunner encodeSeedPasswords(JdbcTemplate jdbc, PasswordEncoder encoder) {
    return args -> {
      List<Map<String, Object>> rows =
          jdbc.queryForList("SELECT ID, PASSWORD_HASH FROM USERS");
      for (Map<String, Object> r : rows) {
        Integer id = ((Number) r.get("ID")).intValue();
        String ph = (String) r.get("PASSWORD_HASH");
        if (ph != null && ph.startsWith("{raw}")) {
          String raw = ph.substring("{raw}".length());
          String encoded = encoder.encode(raw);   // -> "{bcrypt}$2a..."
          jdbc.update("UPDATE USERS SET PASSWORD_HASH=? WHERE ID=?", encoded, id);
        }
      }
    };
  }
}
