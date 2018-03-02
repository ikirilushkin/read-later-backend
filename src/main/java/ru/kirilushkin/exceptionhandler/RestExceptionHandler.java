package ru.kirilushkin.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kirilushkin.response.RestErrorResponse;

@RestControllerAdvice(basePackages = "ru.kirilushkin.rest")
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e.getClass().isAnnotationPresent(ResponseStatus.class)) {
            status =
                    e.getClass().getAnnotation(
                            ResponseStatus.class
                    ).value();
        }

        RestErrorResponse response = new RestErrorResponse(status.value(), e.getMessage());

        return new ResponseEntity<>(response, status);
    }
}
