package com.github.mecharyry.auth;

import android.content.Context;
import android.content.Intent;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthRequester;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

public class AuthenticationManager {

    private final Context context;
    private final OAuthAuthenticator oAuthAuthentication;
    private final OAuthRequester oAuthRequester;
    private final AccessTokenPreferences accessTokenPreferences;
    private final Callback callback;

    public interface Callback {
        void onAuthenticated();
    }

    public static AuthenticationManager newInstance(Context context, Callback callback) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(context);
        OAuthRequester oAuthRequester = new OAuthRequester();
        return new AuthenticationManager(context, oAuthAuthenticator, oAuthRequester, accessTokenPreferences, callback);
    }

    private AuthenticationManager(Context context, OAuthAuthenticator oAuthAuthentication, OAuthRequester oAuthRequester, AccessTokenPreferences accessTokenPreferences, Callback callback) {
        this.context = context;
        this.oAuthAuthentication = oAuthAuthentication;
        this.accessTokenPreferences = accessTokenPreferences;
        this.callback = callback;
        this.oAuthRequester = oAuthRequester;
    }

    private final OAuthRequester.Callback onOAuthRequesterResult = new OAuthRequester.Callback() {
        @Override
        public void onRequesterResult(String result) {
            RequestAccessTokenTask.newInstance(accessTokenCallback, oAuthAuthentication).executeTask(result);
        }
    };

    private final RequestAccessTokenTask.Callback accessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            accessTokenPreferences.saveAccessToken(response);
            callback.onAuthenticated();
        }
    };

    public void authenticate() {
        RequestTokenTask.newInstance(requestTokenCallback, oAuthAuthentication).execute();
    }

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            oAuthRequester.createRequest(context, response);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        oAuthRequester.onOAuthRequesterResult(requestCode, resultCode, data, onOAuthRequesterResult);
    }

    public boolean hasAccessToken() {
        return accessTokenPreferences.hasAccess();
    }
}
