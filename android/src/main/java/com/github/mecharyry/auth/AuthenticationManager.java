package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.github.mecharyry.OAuthRequestor;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

class AuthenticationManager {
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_ACCESS_SECRET = "access_secret";

    private final OAuthAuthenticator oAuthAuthentication;
    private final SharedPreferences preferences;
    private final OAuthRequestor oAuthRequestor;
    private final Activity activity;

    AuthenticationManager(OAuthAuthenticator oAuthAuthentication, Activity activity, SharedPreferences preferences) {
        this.oAuthAuthentication = oAuthAuthentication;
        this.preferences = preferences;
        this.oAuthRequestor = new OAuthRequestor(onOAuthRequesterResult);
        this.activity = activity;
    }

    private final OAuthRequestor.AuthenticatorRequesterResult onOAuthRequesterResult = new OAuthRequestor.AuthenticatorRequesterResult() {
        @Override
        public void onRequesterResult(String result) {
            new RequestAccessTokenTask(accessTokenCallback, oAuthAuthentication).execute(result);
        }
    };

    private final RequestAccessTokenTask.Callback accessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_ACCESS_TOKEN, response.getToken());
            editor.putString(PREF_ACCESS_SECRET, response.getSecret());
            editor.apply();
        }
    };

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            oAuthRequestor.request(activity, response);
        }
    };

    public void authenticate() {
        Toast.makeText(activity, "Opening Browser", Toast.LENGTH_SHORT).show();
        new RequestTokenTask(requestTokenCallback, oAuthAuthentication).execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        oAuthRequestor.onOAuthRequesterResult(requestCode, resultCode, data);
    }

    public boolean hasAccess() {
        return preferences.contains(PREF_ACCESS_TOKEN) && preferences.contains(PREF_ACCESS_SECRET);
    }
}
