package com.weatherapp.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiKeyNotFoundException.class)
    public ProblemDetail handleApiKeyNotFoundError(ApiKeyNotFoundException ex) {
        log.error("Error occurred - ", ex);
        return getProblemDetail(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredApiKeyException.class)
    public ProblemDetail handleKeyExpiredError(ExpiredApiKeyException ex) {
        log.error("Error occurred - ", ex);
        return getProblemDetail(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiKeyRateExceededException.class)
    public ProblemDetail handleRateExceededError(ApiKeyRateExceededException ex) {
        log.error("Error occurred - ", ex);
        return getProblemDetail(HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        log.error("Error occurred - ", ex);
        ProblemDetail problemDetail = getProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        Optional.ofNullable(ex.getParameterMap()).orElseGet(Collections::emptyMap)
                .forEach(problemDetail::setProperty);
        return problemDetail;
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ProblemDetail handleInvalidParameterException(InvalidParameterException ex) {
        log.error("Error occurred - ", ex);
        ProblemDetail problemDetail = getProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        Optional.ofNullable(ex.getParameterMap()).orElseGet(Collections::emptyMap)
                .forEach(problemDetail::setProperty);
        return problemDetail;
    }

    @ExceptionHandler(InternalServerError.class)
    public ProblemDetail handleInternalServerError(InternalServerError ex) {
        log.error("Error occurred - ", ex);
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        log.error("Error occurred - ", ex);
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ProblemDetail getProblemDetail(HttpStatus httpStatus, String message) {
        ProblemDetail problemDetail = getProblemDetail(httpStatus);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    private static ProblemDetail getProblemDetail(HttpStatus httpStatus) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setType(URI.create("http://localhost:8080/v1/errors/".concat(
                httpStatus.getReasonPhrase().toLowerCase().replaceAll(" ", "-")))
        );
        return problemDetail;
    }

}
