package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "/v1/user")
public interface UserService extends IUserService {

    @GetMapping("/me")
    public ResponseEntity<UserDto> findMe(@RequestHeader("Authorization") String token);
}
