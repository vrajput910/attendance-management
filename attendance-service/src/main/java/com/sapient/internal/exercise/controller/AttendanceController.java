package com.sapient.internal.exercise.controller;

import com.sapient.internal.exercise.enums.AttendanceStatus;
import com.sapient.internal.exercise.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/v1")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/absent/{date}")
    public ResponseEntity<?> findAllAbsentOnADate(@PathVariable String date) {
        try {
            return ResponseEntity.ok(attendanceService.findAllAbsentOnADate(LocalDate.parse(date)));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date should be in YYYY-MM-DD format!");
        }
    }

    @GetMapping("/attendance/{userId}")
    public ResponseEntity<?> findByUserIdAndDateBetween(@PathVariable long userId, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            return ResponseEntity.ok(attendanceService.findByUserIdAndDateBetween(userId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date should be in YYYY-MM-DD format!");
        }
    }

    @GetMapping("/attendance/{userId}/{status}")
    public ResponseEntity<?> findByUserIdAndDateBetweenAndStatus(@PathVariable long userId, @PathVariable AttendanceStatus status, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            return ResponseEntity.ok(attendanceService.findByUserIdAndDateBetweenAndStatus(userId, LocalDate.parse(startDate), LocalDate.parse(endDate), status));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Date should be in YYYY-MM-DD format!");
        }
    }

}
