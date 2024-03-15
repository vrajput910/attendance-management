package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.ManualSwipeDto;
import com.sapient.internal.exercise.dto.SwipeDto;
import com.sapient.internal.exercise.entities.SwipeEvent;
import com.sapient.internal.exercise.enums.SwipeType;
import com.sapient.internal.exercise.kafka.KafkaPublisher;
import com.sapient.internal.exercise.kafka.SwipeKafkaEvent;
import com.sapient.internal.exercise.repository.SwipeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class SwipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwipeService.class);

    @Autowired
    private SwipeRepo swipeRepo;

    @Autowired
    private KafkaPublisher kafkaPublisherService;

    public SwipeEvent swipeCard(SwipeDto swipeDto) {
        if (SwipeType.IN.equals(swipeDto.getSwipeType())) {
            return handleSwipeIn(swipeDto);
        } else {
            return handleSwipeOut(swipeDto);
        }
    }

    private SwipeEvent handleSwipeIn(SwipeDto swipeDto) {
        LOGGER.info("Swipe in call with dto: {}", swipeDto);
        String swipeDate = LocalDate.now().toString();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        List<SwipeEvent> swipeEvents = swipeRepo.findByUserIdAndSwipeDate(swipeDto.getUserId(), swipeDate, pageable);
        SwipeEvent swipeEvent;
        if (!swipeEvents.isEmpty()) {
            swipeEvent = swipeEvents.get(0);
            if (swipeEvent.getSwipeOutTime() == null)
                throw new RuntimeException("User is already in!");
        }
        swipeEvent = new SwipeEvent();
        swipeEvent.setUserId(swipeDto.getUserId());
        swipeEvent.setSwipeDate(swipeDate);
        swipeEvent.setSwipeInTime(LocalTime.now().toString());
        swipeEvent.setLocation(swipeDto.getLocation());
        LOGGER.info("{}", swipeEvents);
        return swipeRepo.save(swipeEvent);
    }

    private SwipeEvent handleSwipeOut(SwipeDto swipeDto) {
        LOGGER.info("Swipe out call with dto: {}", swipeDto);
        String swipeDate = LocalDate.now().toString();
        LocalTime outTime = LocalTime.now();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        List<SwipeEvent> swipeEvents = swipeRepo.findByUserIdAndSwipeDate(swipeDto.getUserId(), swipeDate, pageable);
        SwipeEvent swipeEvent;
        if (!swipeEvents.isEmpty()) {
            LOGGER.info("swipe event is present with user: {} and date: {}", swipeDto.getUserId(), swipeDate);
            swipeEvent = swipeEvents.get(0);
            if (swipeEvent.getSwipeOutTime() != null)
                throw new RuntimeException("User is already out!");
            swipeEvent.setSwipeOutTime(outTime.toString());
            swipeEvent.setWorkingHours(getTimeDiffInHours(LocalTime.parse(swipeEvent.getSwipeInTime()), outTime));
        } else {
            LOGGER.info("No swipe event with user: {} and date: {}", swipeDto.getUserId(), swipeDate);
            List<SwipeEvent> userLastSwipeEventList = swipeRepo.findByUserId(swipeDto.getUserId(), pageable);
            if (userLastSwipeEventList.isEmpty()) {
                throw new RuntimeException("User is already out!");
            }
            SwipeEvent userLastSwipeEvent = userLastSwipeEventList.get(0);
            if (userLastSwipeEvent.getSwipeOutTime() != null) {
                LOGGER.info("user is already out at time: {}", userLastSwipeEvent.getSwipeOutTime());
                throw new RuntimeException("User is already out!");
            } else {
                LOGGER.info("this condition is for handling swipe in today and swipe out on next day");
                // Not handling if swipe out is on next to next day
                LocalTime previousOutTime = LocalTime.of(23, 59, 59);
                userLastSwipeEvent.setSwipeOutTime(LocalTime.of(23, 59, 59).toString());
                userLastSwipeEvent.setWorkingHours(getTimeDiffInHours(LocalTime.parse(userLastSwipeEvent.getSwipeInTime()), previousOutTime));
                userLastSwipeEvent = swipeRepo.save(userLastSwipeEvent);
                publishEvent(userLastSwipeEvent);
                swipeEvent = new SwipeEvent();
                swipeEvent.setUserId(swipeDto.getUserId());
                swipeEvent.setSwipeDate(swipeDate);
                swipeEvent.setSwipeInTime(LocalTime.of(0, 0, 1).toString());
                swipeEvent.setSwipeOutTime(outTime.toString());
                swipeEvent.setLocation(swipeDto.getLocation());
                swipeEvent.setWorkingHours(getTimeDiffInHours(LocalTime.of(0, 0, 1), outTime));
            }
        }
        swipeEvent = swipeRepo.save(swipeEvent);
        publishEvent(swipeEvent);
        return swipeEvent;
    }

    private float getTimeDiffInHours(LocalTime inTime, LocalTime outTime) {
        long minutes = inTime.until(outTime, ChronoUnit.MINUTES);
        return (float) minutes / 60;
    }

    private void publishEvent(SwipeEvent swipeEvent) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "id");
        List<SwipeEvent> swipeEvents = swipeRepo.findByUserIdAndSwipeDate(swipeEvent.getUserId(), swipeEvent.getSwipeDate(), pageable);
        float totalWorkingHours = (float) swipeEvents.stream().mapToDouble(SwipeEvent::getWorkingHours).sum();
        Optional<SwipeEvent> firstSwipeEvent = swipeEvents.stream().findFirst();
        SwipeKafkaEvent swipeKafkaEvent = getSwipeKafkaEvent(swipeEvent, firstSwipeEvent, totalWorkingHours);
        kafkaPublisherService.publishSwipeEvent(swipeKafkaEvent);
    }

    private SwipeKafkaEvent getSwipeKafkaEvent(SwipeEvent swipeEvent, Optional<SwipeEvent> firstSwipeEvent, float totalWorkingHours) {
        if (firstSwipeEvent.isEmpty())
            throw new RuntimeException("First swipe event is null! Please contact admin!");
        String firstSwipeInTime = firstSwipeEvent.get().getSwipeInTime();
        String lastSwipeOutTime = swipeEvent.getSwipeOutTime();
        float totalHours = getTimeDiffInHours(LocalTime.parse(firstSwipeInTime), LocalTime.parse(lastSwipeOutTime));
        return new SwipeKafkaEvent(swipeEvent.getUserId(), swipeEvent.getSwipeDate(), LocalTime.parse(firstSwipeInTime), LocalTime.parse(lastSwipeOutTime), swipeEvent.getLocation(), totalWorkingHours, totalHours);
    }

    public SwipeEvent updateSwipeManually(ManualSwipeDto swipeDto) {
        if (SwipeType.IN.equals(swipeDto.getSwipeType())) {
            return handleManualSwipeIn(swipeDto);
        } else {
            return handleManualSwipeOut(swipeDto);
        }
    }

    private SwipeEvent handleManualSwipeIn(ManualSwipeDto swipeDto) {
        LOGGER.info("Manual Swipe in call with dto: {}", swipeDto);
        String swipeDate = swipeDto.getDate().toString();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        List<SwipeEvent> swipeEvents = swipeRepo.findByUserIdAndSwipeDate(swipeDto.getUserId(), swipeDate, pageable);
        SwipeEvent swipeEvent;
        if (!swipeEvents.isEmpty()) {
            swipeEvent = swipeEvents.get(0);
            if (swipeEvent.getSwipeOutTime() == null)
                throw new RuntimeException("User is already in!");
        }
        swipeEvent = new SwipeEvent();
        swipeEvent.setUserId(swipeDto.getUserId());
        swipeEvent.setSwipeDate(swipeDate);
        swipeEvent.setSwipeInTime(swipeDto.getSwipeTime().toString());
        swipeEvent.setLocation(swipeDto.getLocation());
        return swipeRepo.save(swipeEvent);
    }

    private SwipeEvent handleManualSwipeOut(ManualSwipeDto swipeDto) {
        LOGGER.info("Manual Swipe out call with dto: {}", swipeDto);
        String swipeDate = swipeDto.getDate().toString();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        List<SwipeEvent> swipeEvents = swipeRepo.findByUserIdAndSwipeDate(swipeDto.getUserId(), swipeDate, pageable);
        SwipeEvent swipeEvent;
        if (swipeEvents.isEmpty()) {
            throw new RuntimeException("User is already out!");
        }
        swipeEvent = swipeEvents.get(0);
        if (swipeEvent.getSwipeOutTime() != null)
            throw new RuntimeException("User is already out!");
        swipeEvent.setSwipeOutTime(swipeDto.getSwipeTime().toString());
        swipeEvent.setWorkingHours(getTimeDiffInHours(LocalTime.parse(swipeEvent.getSwipeInTime()), swipeDto.getSwipeTime()));
        swipeEvent = swipeRepo.save(swipeEvent);
        publishEvent(swipeEvent);
        return swipeEvent;
    }
}
