package com.hao.common.exception;

public class NoMoreDataException extends Exception {

    public NoMoreDataException() {
        super();
    }

    public NoMoreDataException(String detailMessage) {
        super(detailMessage);
    }

    public NoMoreDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoMoreDataException(Throwable throwable) {
        super(throwable);
    }
}
