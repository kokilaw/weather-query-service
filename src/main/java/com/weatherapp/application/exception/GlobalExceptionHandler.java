package com.weatherapp.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiKeyNotFoundException.class)
    public ProblemDetail handleApiKeyNotFoundError(ApiKeyNotFoundException ex) {
        return ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredApiKeyException.class)
    public ProblemDetail handleKeyExpiredError(ExpiredApiKeyException ex) {
        return ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiKeyRateExceededException.class)
    public ProblemDetail handleRateExceededError(ApiKeyRateExceededException ex) {
        return ProblemDetail.forStatus(HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleInternalServerError(NotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        Optional.ofNullable(ex.getResourceIdMap()).orElseGet(Collections::emptyMap)
                .forEach(problemDetail::setProperty);
        return problemDetail;
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ProblemDetail handleInvalidParameterException(InvalidParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        Optional.ofNullable(ex.getResourceIdMap()).orElseGet(Collections::emptyMap)
                .forEach(problemDetail::setProperty);
        return problemDetail;
    }

    @ExceptionHandler(InternalServerError.class)
    public ProblemDetail handleInternalServerError(InternalServerError ex) {
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
