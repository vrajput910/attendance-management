package com.sapient.internal.exercise.feign;

import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.service.IUserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "/v1")
public interface UserServiceFeignClient extends IUserService {

    @GetMapping("/user/me")
    ResponseEntity<UserDto> findMe(@RequestHeader("Authorization") String token);

    @GetMapping("/admin/user/{id}")
    ResponseEntity<UserDto> findById(@RequestHeader("Authorization") String token, @PathVariable long id);
}
