package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.RegisterUserDto;
import com.sapient.internal.exercise.entities.User;
import com.sapient.internal.exercise.exceptions.UserAlreadyExistException;
import com.sapient.internal.exercise.exceptions.UserNotFoundException;
import com.sapient.internal.exercise.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(RegisterUserDto registerUserDto) {
        userRepo.findByEmail(registerUserDto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistException("User already exist with email: " + u.getEmail());
        });
        User user = getUser(registerUserDto);
        user.setRole(roleService.findByRoleName("user"));
        return userRepo.save(user);
    }

    public User findById(long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists with id: " + id));
    }


    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not exists with email: " + email));
    }

    private User getUser(RegisterUserDto registerUserDto) {
        return new User(registerUserDto.getFirstName(), registerUserDto.getLastName(), registerUserDto.getEmail().toLowerCase(), passwordEncoder.encode(registerUserDto.getPassword()), registerUserDto.getMobile());
    }
}
