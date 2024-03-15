package com.sapient.internal.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SwipeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwipeServiceApplication.class);
    }
}