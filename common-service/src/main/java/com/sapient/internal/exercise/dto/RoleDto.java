package com.sapient.internal.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private long id;

    private String roleName;

    private String description;

    private Set<PermissionDto> permissions;
}
