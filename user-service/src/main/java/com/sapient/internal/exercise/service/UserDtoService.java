package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserDtoService implements IUserService{

    @Override
    public ResponseEntity<UserDto> findMe(String token) {
        /*
        We can implement this later
        For now, assuming that user is available in cache
         */
        return null;
    }
}
