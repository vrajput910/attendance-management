package com.sapient.internal.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long mobile;

    private RoleDto role;
}
