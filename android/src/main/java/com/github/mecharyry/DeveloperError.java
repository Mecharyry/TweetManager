package com.github.mecharyry;

public class DeveloperError extends RuntimeException {
    private final String reason;
    private final Throwable throwable;

    public static DeveloperError because(String reason, Throwable throwable){
        return new DeveloperError(reason, throwable);
    }

    private DeveloperError(String reason, Throwable throwable){
        super(throwable);
        this.reason = reason;
        this.throwable = throwable;
    }
}
