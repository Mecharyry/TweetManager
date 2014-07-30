package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthRequester;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

import java.lang.ref.WeakReference;

class AuthenticationManager {

    private final OAuthAuthenticator oAuthAuthentication;
    private final OAuthRequester oAuthRequester;
    private final Activity activity;
    private final AccessTokenPreferences accessTokenPreferences;
    private static WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onAuthenticated();
    }

    public static AuthenticationManager newInstance(Activity activity, Callback callback) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(activity);
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);

        return new AuthenticationManager(oAuthAuthenticator, activity, accessTokenPreferences, callbackWeakReference);
    }

    AuthenticationManager(OAuthAuthenticator oAuthAuthentication, Activity activity, AccessTokenPreferences accessTokenPreferences, WeakReference<Callback> callbackWeakReference) {
        this.oAuthAuthentication = oAuthAuthentication;
        this.activity = activity;
        this.accessTokenPreferences = accessTokenPreferences;
        this.callbackWeakReference = callbackWeakReference;

        this.oAuthRequester = OAuthRequester.newInstance(onOAuthRequesterResult);
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
            Callback callback = callbackWeakReference.get();
            if (callback != null) {
                callback.onAuthenticated();
            }
        }
    };

    public void authenticate() {
        RequestTokenTask.newInstance(requestTokenCallback, oAuthAuthentication).execute();
    }

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            oAuthRequester.request(activity, response);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        oAuthRequester.onOAuthRequesterResult(requestCode, resultCode, data);
    }

    public boolean hasAccessToken() {
        return accessTokenPreferences.hasAccess();
    }

    public void removeAccessToken() {
        accessTokenPreferences.removeAccessToken();
    }
}
