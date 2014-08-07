package com.github.mecharyry.tweetlist.request;

public class RequestException extends Exception {

    private final String reason;
    private final Throwable throwable;

    public static RequestException because(String reason, Throwable throwable) {
        return new RequestException(reason, throwable);
    }

    public RequestException(String reason, Throwable throwable) {
        this.reason = reason;
        this.throwable = throwable;
    }
}
