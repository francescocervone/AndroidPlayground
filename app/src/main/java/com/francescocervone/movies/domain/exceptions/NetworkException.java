package com.francescocervone.movies.domain.exceptions;


public class NetworkException extends Exception {
    public NetworkException(Throwable throwable) {
        super(throwable);
    }
}
