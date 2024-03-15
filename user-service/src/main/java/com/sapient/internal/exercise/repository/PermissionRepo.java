package com.sapient.internal.exercise.repository;

import com.sapient.internal.exercise.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
