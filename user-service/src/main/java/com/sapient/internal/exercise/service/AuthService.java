package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.LoginDto;
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

    public String login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authenticate.isAuthenticated()) {
            User user = userService.findByEmail(loginDto.getEmail());
            return jwtService.generateToken(user.getEmail());
        } else {
            throw new RuntimeException("Invalid username or password!");
        }
    }
}
