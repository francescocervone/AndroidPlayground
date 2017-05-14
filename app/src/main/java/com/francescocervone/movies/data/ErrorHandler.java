package com.francescocervone.movies.data;


import com.francescocervone.movies.domain.exceptions.BadRequestException;
import com.francescocervone.movies.domain.exceptions.NetworkException;
import com.francescocervone.movies.domain.exceptions.ServiceUnavailableException;

import java.io.IOException;

import retrofit2.HttpException;

public class ErrorHandler {
    public static Throwable convert(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (isBadRequest(httpException)) {
                return new BadRequestException(httpException.code(), httpException.message(), httpException);
            } else if (isServiceUnavailable(httpException)) {
                return new ServiceUnavailableException(httpException.code(), httpException.message(), httpException);
            }

        }
        if (throwable instanceof IOException) {
            return new NetworkException(throwable);
        }
        return throwable;
    }

    private static boolean isServiceUnavailable(HttpException httpException) {
        return httpException.code() >= 500;
    }

    private static boolean isBadRequest(HttpException httpException) {
        return httpException.code() >= 400 && httpException.code() < 500;
    }
}
