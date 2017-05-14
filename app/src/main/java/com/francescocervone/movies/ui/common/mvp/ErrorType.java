package com.francescocervone.movies.ui.common.mvp;


import com.francescocervone.movies.domain.exceptions.BadRequestException;
import com.francescocervone.movies.domain.exceptions.NetworkException;
import com.francescocervone.movies.domain.exceptions.ServiceUnavailableException;

public enum ErrorType {
    GENERIC,
    BAD_REQUEST,
    SERVICE_UNAVAILABLE,
    NETWORK;

    public static ErrorType fromThrowable(Throwable throwable) {
        if (throwable instanceof NetworkException) {
            return ErrorType.NETWORK;
        } else if (throwable instanceof BadRequestException) {
            return ErrorType.BAD_REQUEST;
        } else if (throwable instanceof ServiceUnavailableException) {
            return ErrorType.SERVICE_UNAVAILABLE;
        }
        return ErrorType.GENERIC;
    }
}
