package com.sapient.internal.exercise.controller;

import com.sapient.internal.exercise.enums.AttendanceStatus;
import com.sapient.internal.exercise.service.UserAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/v1")
public class UserAttendanceController {

    @Autowired
    private UserAttendanceService userAttendanceService;

    @GetMapping("/attendance")
    @PreAuthorize("hasAuthority('FIND_SELF_ATTENDANCE')")
    public ResponseEntity<?> findByDateBetween(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            return ResponseEntity.ok(userAttendanceService.findByDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate)));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date should be in YYYY-MM-DD format!");
        }
    }

    @GetMapping("/attendance/{status}")
    @PreAuthorize("hasAuthority('FIND_SELF_ATTENDANCE')")
    public ResponseEntity<?> findByDateBetweenAndStatus(@PathVariable AttendanceStatus status, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            return ResponseEntity.ok(userAttendanceService.findByDateBetweenAndStatus(LocalDate.parse(startDate), LocalDate.parse(endDate), status));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date should be in YYYY-MM-DD format!");
        }
    }

}
