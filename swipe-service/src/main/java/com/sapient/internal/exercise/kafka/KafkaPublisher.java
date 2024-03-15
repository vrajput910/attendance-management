package com.sapient.internal.exercise.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Value("${kafka.swipe.topic}")
    private String swipeEventTopic;

    @Autowired
    private KafkaTemplate<String, SwipeKafkaEvent> kafkaTemplate;

    public void publishSwipeEvent(SwipeKafkaEvent swipeKafkaEvent) {
        kafkaTemplate.send(swipeEventTopic, swipeKafkaEvent);
    }
}
