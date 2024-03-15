package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.entities.Role;
import com.sapient.internal.exercise.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role saveRole(Role role) {
        Optional<Role> roleOptional = roleRepo.findByRoleName(role.getRoleName());
        if (roleOptional.isPresent()) {
            Role savedRole = roleOptional.get();
            savedRole.setDescription(role.getDescription());
            savedRole.setPermissions(role.getPermissions());
            return roleRepo.save(savedRole);
        }
        role.setRoleName(role.getRoleName().toUpperCase());
        return roleRepo.save(role);
    }

    public Role findByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName).orElseThrow(() -> new RuntimeException("Role not exist with name: " + roleName));
    }
}
