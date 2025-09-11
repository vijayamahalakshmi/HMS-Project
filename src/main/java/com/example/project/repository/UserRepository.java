package com.example.project.repository;

import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // optional but fine
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);                 // <-- add this
    Optional<User> findByUsernameOrEmail(String u, String e); // handy for login

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
