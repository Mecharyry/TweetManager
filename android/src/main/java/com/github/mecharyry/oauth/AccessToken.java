package com.github.mecharyry.oauth;

public class AccessToken {
    private final String accessToken;
    private final String accessSecret;

    public AccessToken(String accessToken, String accessSecret) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
    }

    public String getSecret() {
        return accessSecret;
    }

    public String getToken() {
        return accessToken;
    }
}
