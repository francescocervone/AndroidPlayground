package com.francescocervone.movies.domain.exceptions;


public class BadRequestException extends HttpException {
    public BadRequestException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
