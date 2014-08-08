package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.NetworkResponse;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;

import java.lang.ref.WeakReference;

public class RequestAccessTokenTask extends AsyncTask<String, Void, NetworkResponse> {

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
    protected NetworkResponse doInBackground(String... params) {
        return oAuthAuthenticator.retrieveAccessToken(params[0]);
    }

    @Override
    protected void onPostExecute(NetworkResponse response) {
        super.onPostExecute(response);
        AccessToken accessToken = responseToAccessToken(response);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrieved(accessToken);
        }
    }

    public AccessToken responseToAccessToken(NetworkResponse response) {
        try {
            return (AccessToken) response.getResponse();
        } catch (ClassCastException e) {
            throw new ClassCastException("Response type differed from expected.\nExpected: " +
                    AccessToken.class.getSimpleName() + "\nActual: " + response.getResponse().getClass().getSimpleName());
        }
    }
}
