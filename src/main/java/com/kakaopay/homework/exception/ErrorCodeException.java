package com.kakaopay.homework.exception;

public class ErrorCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

}
