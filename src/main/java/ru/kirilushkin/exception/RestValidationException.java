package ru.kirilushkin.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestValidationException extends RuntimeException {

    private String field;

    public RestValidationException(String field) {
        this.field = field;
    }

    public RestValidationException(String message, String field) {
        super(message);
        this.field = field;
    }

    public RestValidationException(String message, Throwable cause, String field) {
        super(message, cause);
        this.field = field;
    }

    public RestValidationException(Throwable cause, String field) {
        super(cause);
        this.field = field;
    }

    public RestValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String field) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.field = field;
    }
}
