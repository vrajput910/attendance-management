package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.component.AppContext;
import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.entities.AttendanceRecord;
import com.sapient.internal.exercise.enums.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserAttendanceService {

    @Autowired
    private AppContext appContext;

    @Autowired
    private AttendanceService attendanceService;

    public List<AttendanceRecord> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        UserDto userDto = appContext.getLoggedInUser();
        if (Optional.ofNullable(userDto).isEmpty())
            throw new RuntimeException("Session expired! Please login again!");
        return attendanceService.findByUserIdAndDateBetween(userDto.getId(), startDate, endDate);
    }

    public List<AttendanceRecord> findByDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, AttendanceStatus attendanceStatus) {
        UserDto userDto = appContext.getLoggedInUser();
        if (Optional.ofNullable(userDto).isEmpty())
            throw new RuntimeException("Session expired! Please login again!");
        return attendanceService.findByUserIdAndDateBetweenAndStatus(userDto.getId(), startDate, endDate, attendanceStatus);
    }
}
