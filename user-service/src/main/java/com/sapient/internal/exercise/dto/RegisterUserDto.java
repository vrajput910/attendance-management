package com.sapient.internal.exercise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotBlank(message = "The field firstName can't be null or empty!")
    private String firstName;

    @NotBlank(message = "The field lastName can't be null or empty!")
    private String lastName;

    @NotBlank(message = "The field email can't be null or empty!")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Please provide a valid email!")
    private String email;

    @NotBlank(message = "The field password can't be null or empty!")
    private String password;

    @NotNull(message = "The field mobile can't be null or empty!")
    private Long mobile;
}
