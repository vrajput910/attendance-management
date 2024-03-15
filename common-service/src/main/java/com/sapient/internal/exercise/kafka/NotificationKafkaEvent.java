package com.sapient.internal.exercise.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationKafkaEvent {

    private long userId;

    private float workingHours;

    private float totalHours;
}
