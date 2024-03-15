package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.feign.UserServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    public UserDto findUserById(long userId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Optional.ofNullable(attributes).isPresent()) {
            String authorization = attributes.getRequest().getHeader("Authorization");

            return userServiceFeignClient.findById(authorization, userId).getBody();
        }
        return null;
    }
}
