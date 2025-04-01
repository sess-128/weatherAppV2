package com.rrtyui.weatherappv2.exception;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(Throwable throwable) {
        super(throwable);
    }
}
