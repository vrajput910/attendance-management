package com.sapient.internal.exercise.repository;

import com.sapient.internal.exercise.entities.AttendanceRecord;
import com.sapient.internal.exercise.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, String> {

    List<AttendanceRecord> findByUserId(long userId);

    AttendanceRecord findByUserIdAndDate(long userId, LocalDate date);

    List<AttendanceRecord> findByAttendanceStatus(AttendanceStatus status);

    List<AttendanceRecord> findByAttendanceStatusAndDate(AttendanceStatus status, LocalDate date);

    //    @Query("FROM AttendanceRecord WHERE userId = :userId and date BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate);

    //    @Query("FROM AttendanceRecord WHERE userId = :userId and attendanceStatus = :status and date BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByUserIdAndAttendanceStatusAndDateBetween(long userId, AttendanceStatus attendanceStatus, LocalDate startDate, LocalDate endDate);

    long count();
}
