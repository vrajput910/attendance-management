package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.ManualSwipeDto;
import com.sapient.internal.exercise.dto.SwipeDto;
import com.sapient.internal.exercise.entities.SwipeEvent;
import com.sapient.internal.exercise.enums.SwipeType;
import com.sapient.internal.exercise.kafka.KafkaPublisher;
import com.sapient.internal.exercise.kafka.SwipeKafkaEvent;
import com.sapient.internal.exercise.repository.SwipeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwipeServiceTests {

    @InjectMocks
    private SwipeService swipeService;

    @Mock
    private SwipeRepo swipeRepo;

    @Mock
    private KafkaPublisher kafkaPublisher;

    private SwipeEvent swipeEvent;

    @BeforeEach
    public void init() {
        swipeEvent = new SwipeEvent("abc", 1, "2024-03-05", "11:30:00", null, null, 0);
    }

    @Test
    public void testSwipeIn() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.IN, "");
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        SwipeEvent returnedSwipeEvent = swipeService.swipeCard(swipeDto);
        assertNotNull(returnedSwipeEvent);
    }

    @Test
    public void testSwipeInException() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.IN, "");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.swipeCard(swipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already in"));
    }

    @Test
    public void testSwipeOut() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        doAnswer(i -> i).when(kafkaPublisher).publishSwipeEvent(any(SwipeKafkaEvent.class));
        SwipeEvent returnedSwipeEvent = swipeService.swipeCard(swipeDto);
        assertNotNull(returnedSwipeEvent.getSwipeOutTime());
    }

    @Test
    public void testSwipeOutException() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        swipeEvent.setSwipeOutTime("14:45:06");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.swipeCard(swipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already out"));
    }

    @Test
    public void testSwipeOutNextDay() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(new ArrayList<>()).thenReturn(List.of(swipeEvent));
        when(swipeRepo.findByUserId(anyLong(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        doAnswer(i -> i).when(kafkaPublisher).publishSwipeEvent(any(SwipeKafkaEvent.class));
        SwipeEvent returnedSwipeEvent = swipeService.swipeCard(swipeDto);
        assertNotNull(returnedSwipeEvent.getSwipeOutTime());
    }

    @Test
    public void testSwipeOutNextDayException1() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());
        when(swipeRepo.findByUserId(anyLong(), any(Pageable.class))).thenReturn(new ArrayList<>());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.swipeCard(swipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already out"));
    }

    @Test
    public void testSwipeOutNextDayException2() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        swipeEvent.setSwipeOutTime("14:45:06");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());
        when(swipeRepo.findByUserId(anyLong(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.swipeCard(swipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already out"));
    }

    @Test
    public void testSwipeOutNextDayException3() {
        SwipeDto swipeDto = new SwipeDto(1L, SwipeType.OUT, "");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());
        when(swipeRepo.findByUserId(anyLong(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.swipeCard(swipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("First swipe event is null! Please contact admin"));
    }

    @Test
    public void testSwipeInManually() {
        ManualSwipeDto manualSwipeDto = new ManualSwipeDto(1L, SwipeType.IN, LocalDate.parse("2024-03-05"), LocalTime.parse("11:30:00"), null);
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        SwipeEvent returnedSwipeEvent = swipeService.updateSwipeManually(manualSwipeDto);
        assertNotNull(returnedSwipeEvent);
    }

    @Test
    public void testSwipeInManuallyException() {
        ManualSwipeDto manualSwipeDto = new ManualSwipeDto(1L, SwipeType.IN, LocalDate.parse("2024-03-05"), LocalTime.parse("11:30:00"), null);
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.updateSwipeManually(manualSwipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already in"));
    }

    @Test
    public void testSwipeOutManually() {
        ManualSwipeDto manualSwipeDto = new ManualSwipeDto(1L, SwipeType.OUT, LocalDate.parse("2024-03-05"), LocalTime.parse("11:30:00"), null);
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        when(swipeRepo.save(any(SwipeEvent.class))).thenReturn(swipeEvent);
        doAnswer(i -> i).when(kafkaPublisher).publishSwipeEvent(any(SwipeKafkaEvent.class));
        SwipeEvent returnedSwipeEvent = swipeService.updateSwipeManually(manualSwipeDto);
        assertNotNull(returnedSwipeEvent.getSwipeOutTime());
    }

    @Test
    public void testSwipeOutManuallyException1() {
        ManualSwipeDto manualSwipeDto = new ManualSwipeDto(1L, SwipeType.OUT, LocalDate.parse("2024-03-05"), LocalTime.parse("11:30:00"), null);
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.updateSwipeManually(manualSwipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already out"));
    }

    @Test
    public void testSwipeOutManuallyException2() {
        ManualSwipeDto manualSwipeDto = new ManualSwipeDto(1L, SwipeType.OUT, LocalDate.parse("2024-03-05"), LocalTime.parse("11:30:00"), null);
        swipeEvent.setSwipeOutTime("14:45:06");
        when(swipeRepo.findByUserIdAndSwipeDate(anyLong(), anyString(), any(Pageable.class))).thenReturn(List.of(swipeEvent));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> swipeService.updateSwipeManually(manualSwipeDto),
                "Expected swipeCard to throw RuntimeException"
        );
        assertTrue(thrown.getLocalizedMessage().contains("User is already out"));
    }
}
