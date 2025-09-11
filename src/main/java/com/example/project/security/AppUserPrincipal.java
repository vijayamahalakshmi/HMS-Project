package com.example.project.security;

import com.example.project.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AppUserPrincipal implements UserDetails {
    private final User user;

    public AppUserPrincipal(User user) {
        this.user = Objects.requireNonNull(user);
    }

    /** Expose your entity if you need it elsewhere (e.g., to build JWT claims). */
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRole() != null ? user.getRole().getName() : "USER";
        // Spring expects "ROLE_..." authority names
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override public String getPassword() { return user.getPasswordHash(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
