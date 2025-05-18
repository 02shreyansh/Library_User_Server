package com.shreyansh.User_Service.db;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;
    private final List<GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.user = user;
        this.authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getCompanyName() {
        return user.getCompanyName();
    }

    public Instant getCreatedAt() {
        return user.getCreatedAt();
    }

    public UserStatus getStatus() {
        return user.getStatus();
    }

    public boolean isVerified() {
        return user.getIsVerified();
    }

    public boolean isEmailVerified() {
        return user.getIsEmailVerified();
    }

    public boolean isPhoneVerified() {
        return user.getIsPhoneVerified();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getIsDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getIsBlocked() && !user.getIsSuspended();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getIsPasswordReset();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive() && 
               user.getIsApproved() && 
               user.getStatus() == UserStatus.APPROVED;
    }

    // Additional security checks
    public boolean hasRole(String role) {
        return authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public boolean hasAnyRole(String... roles) {
        return authorities.stream()
            .anyMatch(a -> {
                for (String role : roles) {
                    if (a.getAuthority().equals("ROLE_" + role)) {
                        return true;
                    }
                }
                return false;
            });
    }

    public User getUser() {
        return user;
    }
}