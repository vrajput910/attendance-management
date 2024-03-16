package com.sapient.internal.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3108906599844180185L;

    private long id;

    private String permissionName;

    private String description;
}
