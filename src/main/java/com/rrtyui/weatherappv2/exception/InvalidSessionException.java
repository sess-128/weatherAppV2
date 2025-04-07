package com.rrtyui.weatherappv2.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String message) {
        super(message);
    }

    public InvalidSessionException() {
    }
}
