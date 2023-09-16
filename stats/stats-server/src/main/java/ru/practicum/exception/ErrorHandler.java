package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ApiError handleBadRequestException(RuntimeException ex) {
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        log.error("Bad request error occurred with code {}. Stack trace: {}. ",
                HttpStatus.BAD_REQUEST.value(),
                stackTrace);
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request", ex.getMessage());
    }
}
