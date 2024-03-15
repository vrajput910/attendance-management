package com.sapient.internal.exercise.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Value("${kafka.notification.topic}")
    private String notificationEventTopic;

    @Autowired
    private KafkaTemplate<String, NotificationKafkaEvent> kafkaTemplate;

    public void publishNotificationEvent(NotificationKafkaEvent notificationKafkaEvent) {
        kafkaTemplate.send(notificationEventTopic, notificationKafkaEvent);
    }
}
