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

    public static AuthenticationManager newInstance(Activity activity, WeakReference<Callback> callbackWeakReference) {
        return new AuthenticationManager(OAuthAuthenticator.newInstance(), activity, AccessTokenPreferences.newInstance(activity), callbackWeakReference);
    }

    private AuthenticationManager(OAuthAuthenticator oAuthAuthentication, Activity activity, AccessTokenPreferences accessTokenPreferences, WeakReference<Callback> callbackWeakReference) {
        this.oAuthAuthentication = oAuthAuthentication;
        WeakReference<OAuthRequester.Callback> weakReference = new WeakReference<OAuthRequester.Callback>(onOAuthRequesterResult);
        this.oAuthRequester = new OAuthRequester(weakReference);
        this.activity = activity;
        this.accessTokenPreferences = accessTokenPreferences;
        this.callbackWeakReference = callbackWeakReference;
    }

    private final OAuthRequester.Callback onOAuthRequesterResult = new OAuthRequester.Callback() {
        @Override
        public void onRequesterResult(String result) {
            WeakReference<RequestAccessTokenTask.Callback> weakReference = new WeakReference<RequestAccessTokenTask.Callback>(accessTokenCallback);
            new RequestAccessTokenTask(weakReference, oAuthAuthentication).execute(result);
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

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            oAuthRequester.request(activity, response);
        }
    };

    public void authenticate() {
        WeakReference<RequestTokenTask.Callback> weakReference = new WeakReference<RequestTokenTask.Callback>(requestTokenCallback);
        new RequestTokenTask(weakReference, oAuthAuthentication).execute();
    }

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
