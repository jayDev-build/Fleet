package com.FYP.Fleet.Models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// NO @Entity here! This is a plain Java class (POJO)
public class SecurityUser implements UserDetails {

    private final User user;

    // Use a single constructor to pass the entity in
    public SecurityUser(User user) {
        this.user = user;
    }

    // Connect the UserDetails methods directly to the User entity fields
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String phoneNumber(){
        return user.getPhone();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Add roles here later if needed
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}