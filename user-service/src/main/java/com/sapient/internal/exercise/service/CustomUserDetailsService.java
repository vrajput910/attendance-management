package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.entities.Permission;
import com.sapient.internal.exercise.entities.Role;
import com.sapient.internal.exercise.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        Role role = user.getRole();
        Set<Permission> permissions = role.getPermissions();
        Set<SimpleGrantedAuthority> userPermissions = permissions
                .stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(userPermissions)
                .build();
    }
}
