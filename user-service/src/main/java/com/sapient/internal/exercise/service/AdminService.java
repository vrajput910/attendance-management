package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.entities.Role;
import com.sapient.internal.exercise.entities.User;
import com.sapient.internal.exercise.exceptions.UserNotFoundException;
import com.sapient.internal.exercise.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createAdmin(User user) {
        Role role = roleService.findByRoleName("admin");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.findByEmail(user.getEmail()).orElseGet(() -> userRepo.save(user));
    }

    public Page<User> findAllUsers(int page, int size) {
        return userRepo.findAll(PageRequest.of(page, size));
    }

    public User findById(long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists with id: " + id));
    }


    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not exists with email: " + email));
    }
}
