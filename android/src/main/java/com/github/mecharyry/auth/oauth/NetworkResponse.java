package com.github.mecharyry.auth.oauth;

public class NetworkResponse {

    private final RequestStatus status;
    private final String response;

    NetworkResponse(RequestStatus status, String response) {
        this.status = status;
        this.response = response;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

}
