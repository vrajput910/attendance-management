package com.sapient.internal.exercise.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwipeKafkaEvent {

    private long userId;

    private String swipeDate;

    private LocalTime firstSwipeIn;

    private LocalTime lastSwipeOut;

    private String location;

    private float workingHours;

    private float totalHours;
}
