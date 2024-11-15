package com.reader.manga.exception.handler;

import com.reader.manga.exception.ExceptionResponseDetails;
import com.reader.manga.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseDetails> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(
                ExceptionResponseDetails.builder()
                        .title("Resource Not Found")
                        .StatusCode(HttpStatus.NOT_FOUND.value())
                        .detailsMessage(ex.getMessage())
                        .detailsDevelopment(ex.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }
}
