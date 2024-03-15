package com.sapient.internal.exercise.controller;

import com.sapient.internal.exercise.dto.ManualSwipeDto;
import com.sapient.internal.exercise.dto.SwipeDto;
import com.sapient.internal.exercise.service.SwipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class SwipeController {

    @Autowired
    private SwipeService swipeService;

    @PostMapping("/swipe")
    @PreAuthorize("hasAuthority('SWIPE_CARD')")
    public ResponseEntity<?> swipe(@Valid @RequestBody SwipeDto swipeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(bindingResult.getFieldError());
        }
        try {
            return ResponseEntity.ok(swipeService.swipeCard(swipeDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/swipe/manual")
    @PreAuthorize("hasAuthority('UPDATE_SWIPE')")
    public ResponseEntity<?> updateSwipe(@Valid @RequestBody ManualSwipeDto manualSwipeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(bindingResult.getFieldError());
        }
        try {
            return ResponseEntity.ok(swipeService.updateSwipeManually(manualSwipeDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
