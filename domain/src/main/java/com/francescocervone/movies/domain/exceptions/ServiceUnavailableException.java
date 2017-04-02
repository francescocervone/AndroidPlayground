package com.francescocervone.movies.domain.exceptions;


public class ServiceUnavailableException extends HttpException {
    public ServiceUnavailableException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
