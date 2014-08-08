package com.github.mecharyry.auth.oauth;

public class NetworkResponse {

    private final RequestStatus status;
    private final Object response;

    NetworkResponse(RequestStatus status, Object response) {
        this.status = status;
        this.response = response;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Object getResponse() {
        return response;
    }
}
