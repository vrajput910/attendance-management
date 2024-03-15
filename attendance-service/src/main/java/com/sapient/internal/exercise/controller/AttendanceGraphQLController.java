package com.sapient.internal.exercise.controller;

import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.entities.AttendanceRecord;
import com.sapient.internal.exercise.enums.AttendanceStatus;
import com.sapient.internal.exercise.service.AttendanceService;
import com.sapient.internal.exercise.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AttendanceGraphQLController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceGraphQLController.class);

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @QueryMapping
    @PreAuthorize("hasAuthority('FIND_ATTENDANCE')")
    public List<AttendanceRecord> findAllAbsentOnADate(@Argument LocalDate date) {
        return attendanceService.findAllAbsentOnADate(date);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('FIND_ATTENDANCE')")
    public List<AttendanceRecord> findByUserIdAndDateBetween(@Argument long userId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
        return attendanceService.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('FIND_ATTENDANCE')")
    public List<AttendanceRecord> findByUserIdAndDateBetweenAndStatus(@Argument long userId, @Argument AttendanceStatus status, @Argument LocalDate startDate, @Argument LocalDate endDate) {
        return attendanceService.findByUserIdAndDateBetweenAndStatus(userId, startDate, endDate, status);
    }

    @SchemaMapping(typeName = "Attendance")
    public UserDto user(AttendanceRecord attendanceRecord) {
        return userService.findUserById(attendanceRecord.getUserId());
    }
}
