package com.sapient.internal.exercise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roleName;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public Role(String roleName, String description, Set<Permission> permissions) {
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
    }
}
