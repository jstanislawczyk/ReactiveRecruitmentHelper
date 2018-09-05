package com.reactiverecruitmenthelper.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse notFoundHandler(NotFoundException notFoundException) {
        log.info("ReactiveRecruitmentHelper | {}", notFoundException.getMessage());
        return new ErrorResponse(NOT_FOUND.value(), notFoundException.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse conflictHandler(ConflictException conflictException) {
        log.info("ReactiveRecruitmentHelper | {}", conflictException.getMessage());
        return new ErrorResponse(CONFLICT.value(), conflictException.getMessage());
    }
}
