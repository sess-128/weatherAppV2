package com.rrtyui.weatherappv2.dto.user;

public record AuthResult(boolean passed, String errorMessage) {
    public static AuthResult complete() {
        return new AuthResult(true, null);
    }

    public static AuthResult failure(String errorMessage) {
        return new AuthResult(false, errorMessage);
    }
}
