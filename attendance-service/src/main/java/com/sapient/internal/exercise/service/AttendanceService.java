package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.entities.AttendanceRecord;
import com.sapient.internal.exercise.enums.AttendanceStatus;
import com.sapient.internal.exercise.kafka.KafkaPublisher;
import com.sapient.internal.exercise.kafka.NotificationKafkaEvent;
import com.sapient.internal.exercise.kafka.SwipeKafkaEvent;
import com.sapient.internal.exercise.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaPublisher kafkaPublisher;

    public void storeAttendance(SwipeKafkaEvent swipeKafkaEvent) {
        LOGGER.info("totalHours: {}", swipeKafkaEvent.getTotalHours());
        AttendanceStatus attendanceStatus = getStatus(swipeKafkaEvent.getTotalHours());
        LOGGER.info("attendanceStatus: {}", attendanceStatus);
        LocalDate date = LocalDate.parse(swipeKafkaEvent.getSwipeDate());
        LOGGER.info("date: {}", date);
        AttendanceRecord attendanceRecord = attendanceRepository.findByUserIdAndDate(swipeKafkaEvent.getUserId(), date);
        LOGGER.info("attendanceRecord: {}", attendanceRecord);
        if (attendanceRecord == null) {
            attendanceRecord = new AttendanceRecord();
            attendanceRecord.setDate(date);
            attendanceRecord.setUserId(swipeKafkaEvent.getUserId());
            attendanceRecord.setFirstSwipeIn(swipeKafkaEvent.getFirstSwipeIn());
            attendanceRecord.setLocation(swipeKafkaEvent.getLocation());
        }
        attendanceRecord.setLastSwipeOut(swipeKafkaEvent.getLastSwipeOut());
        attendanceRecord.setWorkingHours(swipeKafkaEvent.getWorkingHours());
        attendanceRecord.setTotalHours(swipeKafkaEvent.getTotalHours());
        attendanceRecord.setAttendanceStatus(attendanceStatus);
        publishNotificationEvent(attendanceRepository.save(attendanceRecord));
    }

    private void publishNotificationEvent(AttendanceRecord attendanceRecord) {
        NotificationKafkaEvent notificationKafkaEvent = new NotificationKafkaEvent(attendanceRecord.getUserId(), attendanceRecord.getWorkingHours(), attendanceRecord.getTotalHours());
        kafkaPublisher.publishNotificationEvent(notificationKafkaEvent);
    }

    private AttendanceStatus getStatus(float totalHours) {
        if (totalHours < 4)
            return AttendanceStatus.ABSENT;
        else if (totalHours < 8)
            return AttendanceStatus.HALF_DAY;
        else
            return AttendanceStatus.PRESENT;
    }

    public List<AttendanceRecord> findAllAbsentOnADate(LocalDate date) {
        return attendanceRepository.findByAttendanceStatusAndDate(AttendanceStatus.ABSENT, date);
    }

    public List<AttendanceRecord> findByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public List<AttendanceRecord> findByUserIdAndDateBetweenAndStatus(long userId, LocalDate startDate, LocalDate endDate, AttendanceStatus status) {
        return attendanceRepository.findByUserIdAndAttendanceStatusAndDateBetween(userId, status, startDate, endDate);
    }

    public List<AttendanceRecord> lastWeekAttendanceForAUser(long userId) {
        LocalDate startOfLastWeek = ZonedDateTime.now().minusWeeks(1).with(DayOfWeek.MONDAY).toLocalDate();
        System.out.println(startOfLastWeek);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);
        System.out.println(endOfLastWeek);
        return attendanceRepository.findByUserIdAndDateBetween(userId, startOfLastWeek, endOfLastWeek);
    }

    public List<AttendanceRecord> currentWeekAttendanceForAUser(long userId) {
        LocalDate startOfLastWeek = ZonedDateTime.now().with(DayOfWeek.MONDAY).toLocalDate();
        System.out.println(startOfLastWeek);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);
        System.out.println(endOfLastWeek);
        return attendanceRepository.findByUserIdAndDateBetween(userId, startOfLastWeek, endOfLastWeek);
    }

}
