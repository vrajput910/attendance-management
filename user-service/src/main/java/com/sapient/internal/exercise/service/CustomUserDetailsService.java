package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private UserService userService;

    @Autowired
    private CacheService<String, UserDto> userCacheService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userCacheService.fetchFromValueOperations(email);
        if (Optional.ofNullable(userDto).isPresent()) {
            LOGGER.info("Fetched from cache!");
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userDto.getEmail())
                    .password(userDto.getPassword())
                    .authorities(userDto.getRole().getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName())).collect(Collectors.toSet()))
                    .build();
        }

        User user = userService.findByEmail(email);
        LOGGER.info("Fetched from DB!");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName())).collect(Collectors.toSet()))
                .build();
    }
}
