package com.sapient.internal.exercise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.internal.exercise.dto.LoginDto;
import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CacheService<String, UserDto> userCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    public String login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            if (authenticate.isAuthenticated()) {
                User user = userService.findByEmail(loginDto.getEmail());
                String token = jwtService.generateToken(user.getEmail());
                userCacheService.saveInValueOperations(user.getEmail(), objectMapper.convertValue(user, UserDto.class));
                return token;
            } else {
                throw new RuntimeException("Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password!");
        }
    }
}
