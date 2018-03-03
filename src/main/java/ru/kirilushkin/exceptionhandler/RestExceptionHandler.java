package ru.kirilushkin.exceptionhandler;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kirilushkin.response.RestErrorResponse;
import ru.kirilushkin.response.ValidationErrorResponse;

@RestControllerAdvice(basePackages = "ru.kirilushkin.rest")
public class RestExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ValidationErrorResponse response = new ValidationErrorResponse(
                bindingResult.getFieldError().getField(),
                messageSource.getMessage(
                        bindingResult.getFieldError().getDefaultMessage(),
                        new Object[]{},
                        null
                ),
                bindingResult.getFieldError().getDefaultMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
