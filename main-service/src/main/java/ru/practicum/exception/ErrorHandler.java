package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, InvalidStateException.class})
    public ApiError handleBadRequestException(RuntimeException ex) {
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        log.error("Bad request error occurred with code {}. Stack trace: {}. ",
                HttpStatus.BAD_REQUEST.value(),
                stackTrace);
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();
        String message = "Required parameter '" + parameterName + "' of type '" + parameterType + "' is missing";
        log.error("Bad request error occurred with code {}. Message: {}. ",
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrect request parameter", message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = "Validation failed for one or more request parameters";
        log.error("Bad request error occurred with code {}. Message: {}. ",
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ObjectNotFoundException.class})
    public ApiError handleNotFoundException(ObjectNotFoundException e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        log.error("Not found error occurred with code {}. Stack trace: {}. ",
                HttpStatus.NOT_FOUND.value(),
                stackTrace);
        return new ApiError(HttpStatus.NOT_FOUND, "Resource not found", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ObjectAlreadyExistsException.class})
    public ApiError handleEmailAlreadyExistsException(ObjectAlreadyExistsException e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        log.error("Email already exists error occurred with code {}. Stack trace: {}. ",
                HttpStatus.CONFLICT.value(),
                stackTrace);
        return new ApiError(HttpStatus.CONFLICT, "Resource conflict", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        log.error("Constraint violation error occurred with code {}. Stack trace: {}. ",
                HttpStatus.CONFLICT.value(),
                stackTrace);
        return new ApiError(HttpStatus.CONFLICT, "Resource conflict", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ApiError handleInternalServerError(Throwable ex) {
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        log.error("Internal server error occurred with code {}. Stack trace: {}. ",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                stackTrace);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage());
    }
}