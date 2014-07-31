package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;

import java.lang.ref.WeakReference;

public class RequestAccessTokenTask extends AsyncTask<String, Void, AccessToken> {

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrieved(AccessToken response);
    }

    public static RequestAccessTokenTask newInstance(Callback callback, OAuthAuthenticator oAuthAuthenticator) {
        WeakReference<Callback> weakReference = new WeakReference<Callback>(callback);
        return new RequestAccessTokenTask(weakReference, oAuthAuthenticator);
    }

    private RequestAccessTokenTask(WeakReference<Callback> callbackWeakReference, OAuthAuthenticator oAuthAuthenticator) {
        this.callbackWeakReference = callbackWeakReference;
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    public void executeTask(String verifier) {
        this.execute(verifier);
    }

    @Override
    protected AccessToken doInBackground(String... params) {
        return oAuthAuthenticator.retrieveAccessToken(params[0]);
    }

    @Override
    protected void onPostExecute(AccessToken accessToken) {
        super.onPostExecute(accessToken);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrieved(accessToken);
        }
    }
}
