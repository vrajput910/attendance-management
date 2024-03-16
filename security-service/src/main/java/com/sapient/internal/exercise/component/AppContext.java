package com.sapient.internal.exercise.component;

import com.sapient.internal.exercise.dto.UserDto;
import com.sapient.internal.exercise.service.CacheService;
import com.sapient.internal.exercise.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppContext.class);

    @Autowired
    private CacheService<String, UserDto> userCacheService;

    @Autowired
    private CacheService<String, String> localCacheService;

    @Autowired
    private IUserService userService;

    public UserDto getLoggedInUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        UserDto userDto = userCacheService.fetchFromValueOperations(email);
        if (Optional.ofNullable(userDto).isEmpty()) {
            LOGGER.info("User not available in cache trying to fetch from user-service!");
            userDto = userService.findMe(localCacheService.fetchFromValueOperations(email)).getBody();
        }
        return userDto;
    }
}
