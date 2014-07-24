package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthRequester;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

class AuthenticationManager {

    private final OAuthAuthenticator oAuthAuthentication;
    private final OAuthRequester oAuthRequester;
    private final Activity activity;
    private final AccessTokenPreferences accessTokenPreferences;

    public static AuthenticationManager newInstance(Activity activity) {
        return new AuthenticationManager(OAuthAuthenticator.newInstance(), activity, AccessTokenPreferences.newInstance(activity));
    }

    AuthenticationManager(OAuthAuthenticator oAuthAuthentication, Activity activity, AccessTokenPreferences accessTokenPreferences) {
        this.oAuthAuthentication = oAuthAuthentication;
        this.oAuthRequester = new OAuthRequester(onOAuthRequesterResult);
        this.activity = activity;
        this.accessTokenPreferences = accessTokenPreferences;
    }

    private final OAuthRequester.AuthenticatorRequesterResult onOAuthRequesterResult = new OAuthRequester.AuthenticatorRequesterResult() {
        @Override
        public void onRequesterResult(String result) {
            new RequestAccessTokenTask(accessTokenCallback, oAuthAuthentication).execute(result);
        }
    };

    private final RequestAccessTokenTask.Callback accessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            accessTokenPreferences.saveAccessToken(response);
        }
    };

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            oAuthRequester.request(activity, response);
        }
    };

    public void authenticate() {
        new RequestTokenTask(requestTokenCallback, oAuthAuthentication).execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        oAuthRequester.onOAuthRequesterResult(requestCode, resultCode, data);
    }

    public boolean hasAccessToken() {
        return accessTokenPreferences.hasAccess();
    }
}
