package com.github.mecharyry.tweetlist.request;

public class RequestException extends Exception {

    public static RequestException because(String reason, Throwable throwable) {
        return new RequestException(reason, throwable);
    }

    public RequestException(String reason, Throwable throwable) {
        super(reason, throwable);
    }
}
