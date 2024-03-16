package com.sapient.internal.exercise.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8839242932025229267L;

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long mobile;

    private RoleDto role;
}
