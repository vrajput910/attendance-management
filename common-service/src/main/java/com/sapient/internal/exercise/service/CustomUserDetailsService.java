package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private LocalCacheService localCacheService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String token = (String) localCacheService.fetchFromCache(email);
        ResponseEntity<UserDto> userResponseEntity = userService.findMe("Bearer " + token);
        if (userResponseEntity.getStatusCode().is2xxSuccessful()) {
            UserDto userDto = userResponseEntity.getBody();
            if (Optional.ofNullable(userDto).isPresent()) {
                return User.builder()
                        .username(userDto.getEmail())
                        .password(userDto.getPassword())
                        .authorities(userDto.getRole().getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName())).collect(Collectors.toSet()))
                        .build();
            }
        }
        return null;
    }
}
