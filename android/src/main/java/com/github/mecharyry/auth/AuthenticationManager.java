package com.github.mecharyry.auth;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthRequester;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

import java.lang.ref.WeakReference;

public class AuthenticationManager {

    private final OAuthAuthenticator oAuthAuthentication;
    private final OAuthRequester oAuthRequester;
    private final AccessTokenPreferences accessTokenPreferences;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onAuthenticated();
    }

    public static AuthenticationManager newInstance(AuthenticationFragment fragment, Callback callback) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(fragment.getActivity());
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        OAuthRequester oAuthRequester = OAuthRequester.newInstance(fragment);
        return new AuthenticationManager(oAuthAuthenticator, oAuthRequester, accessTokenPreferences, callbackWeakReference);
    }

    private AuthenticationManager(OAuthAuthenticator oAuthAuthentication, OAuthRequester oAuthRequester, AccessTokenPreferences accessTokenPreferences, WeakReference<Callback> callbackWeakReference) {
        this.oAuthAuthentication = oAuthAuthentication;
        this.accessTokenPreferences = accessTokenPreferences;
        this.callbackWeakReference = callbackWeakReference;
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
            oAuthRequester.request(response);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        oAuthRequester.onOAuthRequesterResult(requestCode, resultCode, data, onOAuthRequesterResult);
    }

    public boolean hasAccessToken() {
        return accessTokenPreferences.hasAccess();
    }
}
