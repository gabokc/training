package dev.gabrielmumo.kc.lab.kafka.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler
    public ResponseEntity<?> handleException(MethodArgumentNotValidException exception) {
        var errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + " - " + e.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));

        log.error("Error message: {}", errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
