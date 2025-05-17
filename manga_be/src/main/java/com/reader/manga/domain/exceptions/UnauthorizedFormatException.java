package com.reader.manga.domain.exceptions;

public class UnauthorizedFormatException extends RuntimeException {
    public UnauthorizedFormatException(String message) {
        super(message);
    }
}
