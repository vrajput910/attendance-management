package com.sapient.internal.exercise.dto;

import com.sapient.internal.exercise.enums.SwipeType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SwipeDto {

    private Long userId;

    @NotNull(message = "Please provide swipeType!")
    private SwipeType swipeType;

    private String location;
}
