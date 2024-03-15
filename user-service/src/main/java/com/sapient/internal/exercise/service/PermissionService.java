package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.entities.Permission;
import com.sapient.internal.exercise.repository.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    public Permission savePermission(Permission permission) {
        Optional<Permission> permissionOptional = permissionRepo.findByPermissionName(permission.getPermissionName());
        if (permissionOptional.isEmpty()) {
            permission.setPermissionName(permission.getPermissionName().toUpperCase());
            return permissionRepo.save(permission);
        } else
            return permissionOptional.get();
    }
}
