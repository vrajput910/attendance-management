package com.sapient.internal.exercise.exceptions;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5546002205325197524L;

    public UserAlreadyExistException(String msg) {
        super(msg);
    }

}
