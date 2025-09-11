package com.example.project.security;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

  private final UserRepository users;

  public AppUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User u = users.findByEmail(usernameOrEmail)
        .orElseGet(() -> users.findByUsername(usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    return new AppUserPrincipal(u); // <-- important
  }
}
