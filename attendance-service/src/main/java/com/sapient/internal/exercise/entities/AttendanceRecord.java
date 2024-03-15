package com.sapient.internal.exercise.entities;

import com.sapient.internal.exercise.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private LocalDate date;

    private LocalTime firstSwipeIn;

    private LocalTime lastSwipeOut;

    private String location;

    private float workingHours;

    private float totalHours;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
}
