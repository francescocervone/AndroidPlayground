package com.francescocervone.movies.domain.exceptions;


import java.io.IOException;

public class HttpException extends IOException {
    private int mCode;
    public HttpException(int code, String message, Throwable throwable) {
        super(message, throwable);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
