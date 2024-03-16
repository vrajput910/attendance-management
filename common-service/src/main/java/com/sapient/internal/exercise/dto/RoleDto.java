package com.sapient.internal.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6965307688789170768L;

    private long id;

    private String roleName;

    private String description;

    private Set<PermissionDto> permissions;
}
