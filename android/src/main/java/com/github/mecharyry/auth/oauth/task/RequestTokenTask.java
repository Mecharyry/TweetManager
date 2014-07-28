package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.OAuthAuthenticator;

import java.lang.ref.WeakReference;

public class RequestTokenTask extends AsyncTask<Void, Void, String> {

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrieved(String response);
    }

    public static RequestTokenTask newInstance(Callback callback, OAuthAuthenticator oAuthAuthenticator) {
        WeakReference<Callback> weakReference = new WeakReference<Callback>(callback);
        return new RequestTokenTask(weakReference, oAuthAuthenticator);
    }

    private RequestTokenTask(WeakReference<Callback> callbackWeakReference, OAuthAuthenticator oAuthAuthenticator) {
        this.callbackWeakReference = callbackWeakReference;
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    @Override
    protected String doInBackground(Void... params) {
        return oAuthAuthenticator.retrieveAuthenticationUrl();
    }

    @Override
    protected void onPostExecute(String response) {
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrieved(response);
        }
    }
}
