package com.sapient.internal.exercise.controller;

import com.sapient.internal.exercise.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all/user")
    @PreAuthorize("hasAuthority('FIND_ALL')")
    public ResponseEntity<?> findAll(@RequestParam int page, @RequestParam int size) {
        try {
            return ResponseEntity.ok(adminService.findAllUsers(page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('FIND_USER')")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(adminService.findById(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
