package com.sapient.internal.exercise.dto;

import com.sapient.internal.exercise.enums.SwipeType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManualSwipeDto {

    @NotNull(message = "Please provide userId!")
    private Long userId;

    @NotNull(message = "Please provide swipeType!")
    private SwipeType swipeType;

    @NotNull(message = "Please provide date!")
    private LocalDate date;

    @NotNull(message = "Please provide swipeTime!")
    private LocalTime swipeTime;

    private String location;
}
