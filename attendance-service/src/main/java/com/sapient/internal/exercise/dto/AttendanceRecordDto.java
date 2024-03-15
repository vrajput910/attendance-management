package com.sapient.internal.exercise.dto;

import com.sapient.internal.exercise.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecordDto {

    private String id;

    private UserDto user;

    private String date;

    private float totalHours;

    private AttendanceStatus status;

}
