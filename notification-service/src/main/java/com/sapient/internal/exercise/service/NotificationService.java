package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.kafka.NotificationKafkaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(NotificationKafkaEvent notificationKafkaEvent) {
        //TODO send mail or notification to user
        String msg = "You swiped out before completing 8 hours! Your total hours for the day " + notificationKafkaEvent.getTotalHours();
        LOGGER.info(msg);
        LOGGER.info("Notification sent to user: {}", notificationKafkaEvent.getUserId());
    }
}
