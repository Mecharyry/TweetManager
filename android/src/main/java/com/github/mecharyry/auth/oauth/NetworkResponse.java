package com.github.mecharyry.auth.oauth;

public class NetworkResponse <T>{

    private final RequestStatus status;
    private final T response;

    NetworkResponse(RequestStatus status, T response) {
        this.status = status;
        this.response = response;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public T getResponse() {
        return response;
    }
}
