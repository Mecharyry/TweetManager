package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.TwitterManagerActivity;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthWebViewActivity;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

public class AuthenticationManager {

    public static final int REQUEST_CODE = 100;
    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private final Context context;
    private final OAuthAuthenticator oAuthAuthentication;
    private final AccessTokenPreferences accessTokenPreferences;

    public interface NotifyActivity {
        void startWebView(Intent intent, TwitterManagerActivity.Callback callback);

        void onAuthenticated();
    }

    public static AuthenticationManager newInstance(Context context) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(context);
        return new AuthenticationManager(context, oAuthAuthenticator, accessTokenPreferences);
    }

    private AuthenticationManager(Context context, OAuthAuthenticator oAuthAuthentication, AccessTokenPreferences accessTokenPreferences) {
        this.context = context;
        this.oAuthAuthentication = oAuthAuthentication;
        this.accessTokenPreferences = accessTokenPreferences;
    }

    public void authenticateUser() {
        RequestTokenTask.newInstance(requestTokenCallback, oAuthAuthentication).execute();
    }

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            startWebView(response);
        }
    };

    private void startWebView(String url) {
        Intent intent = new Intent(context, OAuthWebViewActivity.class);
        intent.putExtra(OAuthWebViewActivity.EXTRA_REQUEST_URL, url);

        try {
            NotifyActivity callback = (NotifyActivity) context;
            callback.startWebView(intent, onWebViewResponse);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Callback.");
        }
    }

    private final TwitterManagerActivity.Callback onWebViewResponse = new TwitterManagerActivity.Callback() {

        @Override
        public void onWebViewResponse(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                String verifier = data.getStringExtra(OAUTH_VERIFIER);
                requestAccessToken(verifier);
            }
        }
    };

    private void requestAccessToken(String result) {
        RequestAccessTokenTask.newInstance(accessTokenCallback, oAuthAuthentication).executeTask(result);
    }

    private final RequestAccessTokenTask.Callback accessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            accessTokenPreferences.saveAccessToken(response);
            notifyActivityUserAuthenticated();
        }
    };

    private void notifyActivityUserAuthenticated() {
        try {
            NotifyActivity callback = (NotifyActivity) context;
            callback.onAuthenticated();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Callback.");
        }
    }

    public void authenticated(){
        notifyActivityUserAuthenticated();
    }

    public boolean hasAccessToken() {
        return accessTokenPreferences.hasAccess();
    }
}
