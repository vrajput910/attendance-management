package com.sapient.internal.exercise.repository;

import com.sapient.internal.exercise.entities.SwipeEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SwipeRepo extends MongoRepository<SwipeEvent, Long> {

    List<SwipeEvent> findByUserId(long userId, Pageable pageable);

    List<SwipeEvent> findByUserIdAndSwipeDate(long userId, String swipeDate, Pageable pageable);
}
