package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    public ResponseEntity<UserDto> findMe(String token);
}
