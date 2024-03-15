package com.sapient.internal.exercise.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class SwipeEvent {
    @Id
    private String id;

    private long userId;

    private String swipeDate;

    private String swipeInTime;

    private String swipeOutTime;

    private String location;

    private float workingHours;
}
