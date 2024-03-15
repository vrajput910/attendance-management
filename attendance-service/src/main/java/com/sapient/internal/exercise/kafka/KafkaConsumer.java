package com.sapient.internal.exercise.kafka;

import com.sapient.internal.exercise.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private AttendanceService attendanceService;

    @KafkaListener(topics = "${kafka.swipe.topic}", groupId = "${kafka.swipe.group.id}")
    public void handleSwipe(SwipeKafkaEvent swipeKafkaEvent) {
        LOGGER.info("message received: {}", swipeKafkaEvent);
        attendanceService.storeAttendance(swipeKafkaEvent);
    }
}
