package com.example.chataiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(RuntimeException ex) {

        return Map.of(
                "timestamp", LocalDateTime.now(),
                "message", ex.getMessage()
        );
    }
}