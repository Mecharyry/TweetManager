package com.github.mecharyry.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.oauth.AccessToken;
import com.github.mecharyry.oauth.OAuthAuthenticator;

import java.lang.ref.WeakReference;

public class RequestAccessTokenTask extends AsyncTask<String, Void, AccessToken>{

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrieved(AccessToken response);
    }

    public RequestAccessTokenTask(Callback callback, OAuthAuthenticator oAuthAuthenticator) {
        callbackWeakReference = new WeakReference<Callback>(callback);
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    @Override
    protected AccessToken doInBackground(String... params) {
        return oAuthAuthenticator.retrieveAccessToken(params[0]);
    }

    @Override
    protected void onPostExecute(AccessToken accessToken) {
        super.onPostExecute(accessToken);
        Callback callback = callbackWeakReference.get();
        if(callback != null){
            callback.onRetrieved(accessToken);
        }
    }
}
