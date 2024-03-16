package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private CacheService<String, String> localCacheService;

    @Autowired
    private CacheService<String, UserDto> userCacheService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userCacheService.fetchFromValueOperations(email);
        if (Optional.ofNullable(userDto).isPresent()) {
            LOGGER.info("Fetched from cache!");
            return User.builder()
                    .username(userDto.getEmail())
                    .password(userDto.getPassword())
                    .authorities(userDto.getRole().getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName())).collect(Collectors.toSet()))
                    .build();
        }
        String token = localCacheService.fetchFromValueOperations(email);
        ResponseEntity<UserDto> userResponseEntity = userService.findMe("Bearer " + token);
        LOGGER.info("Fetch from user-service!");
        if (userResponseEntity.getStatusCode().is2xxSuccessful()) {
            userDto = userResponseEntity.getBody();
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
