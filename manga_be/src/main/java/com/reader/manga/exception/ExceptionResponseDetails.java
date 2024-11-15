package com.reader.manga.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponseDetails {
    private String title;
    private int StatusCode;
    private String detailsMessage;
    private LocalDateTime timestamp;
    private String detailsDevelopment;
}
