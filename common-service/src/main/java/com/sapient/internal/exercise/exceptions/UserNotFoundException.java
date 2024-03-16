package com.sapient.internal.exercise.exceptions;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1664866141458424070L;

    public UserNotFoundException(String msg) {
        super(msg);
    }

}
