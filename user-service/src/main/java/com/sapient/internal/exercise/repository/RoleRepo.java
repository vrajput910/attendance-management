package com.sapient.internal.exercise.repository;

import com.sapient.internal.exercise.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);
}
