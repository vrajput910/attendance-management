package com.sapient.internal.exercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.internal.exercise.dto.ManualSwipeDto;
import com.sapient.internal.exercise.dto.SwipeDto;
import com.sapient.internal.exercise.enums.SwipeType;
import com.sapient.internal.exercise.service.JwtService;
import com.sapient.internal.exercise.service.LocalCacheService;
import com.sapient.internal.exercise.service.SwipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SwipeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SwipeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwipeService swipeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private LocalCacheService localCacheService;

    @Test
    public void swipeOk() throws Exception {
        mockMvc.perform(post("/v1/swipe").content(asJsonString(new SwipeDto(1L, SwipeType.IN, "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void swipeBadRequest() throws Exception {
        mockMvc.perform(post("/v1/swipe").content(asJsonString(new SwipeDto(null, SwipeType.IN, "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void swipeManualOk() throws Exception {
        mockMvc.perform(post("/v1/swipe/manual").content(asJsonString(new ManualSwipeDto(1L, SwipeType.IN, LocalDate.now(), LocalTime.now(), "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void swipeManualBadRequest() throws Exception {
        mockMvc.perform(post("/v1/swipe/manual").content(asJsonString(new ManualSwipeDto(1L, SwipeType.IN, null, LocalTime.now(), "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(obj);
    }
}
