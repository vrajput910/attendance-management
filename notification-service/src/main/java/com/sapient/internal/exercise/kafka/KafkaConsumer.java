package com.sapient.internal.exercise.kafka;

import com.sapient.internal.exercise.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "${kafka.notification.topic}", groupId = "${kafka.notification.group.id}")
    public void handleNotification(NotificationKafkaEvent notificationKafkaEvent) {
        LOGGER.info("message received: {}", notificationKafkaEvent);
        notificationService.sendNotification(notificationKafkaEvent);
    }
}
