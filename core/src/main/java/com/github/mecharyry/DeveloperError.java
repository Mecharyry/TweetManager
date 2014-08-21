package com.github.mecharyry;

public class DeveloperError extends RuntimeException {

    public static DeveloperError because(String reason, Throwable throwable) {
        return new DeveloperError(reason, throwable);
    }

    private DeveloperError(String reason, Throwable throwable) {
        super(reason, throwable);
    }
}
