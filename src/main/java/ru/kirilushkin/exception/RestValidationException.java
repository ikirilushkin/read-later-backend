package ru.kirilushkin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestValidationException extends RuntimeException {

    public RestValidationException() {
        super();
    }

    public RestValidationException(String message) {
        super(message);
    }

    public RestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestValidationException(Throwable cause) {
        super(cause);
    }

    protected RestValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
