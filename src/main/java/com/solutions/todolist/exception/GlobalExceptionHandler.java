package com.solutions.todolist.exception;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFoundException(final ResourceNotFoundException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setInstance(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
        problemDetail.setProperty("value", exception.getValue());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidRequestException(final MethodArgumentNotValidException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getBody().getDetail());

        problemDetail.setTitle("Bad Request");
        problemDetail.setInstance(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
        problemDetail.setProperty("value", exception.getParameter().getParameter().getName());
        exception.getFieldErrors().forEach(fieldError gs -> problemDetail.setProperty(String.format("value:%s", fieldError.getField()), fieldError.getDefaultMessage()));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(final IllegalArgumentException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        problemDetail.setTitle("Bad Request");
        problemDetail.setInstance(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri());
        problemDetail.setProperty("value", exception.getCause());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
