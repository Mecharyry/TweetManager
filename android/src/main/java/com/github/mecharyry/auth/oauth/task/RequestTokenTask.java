package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.NetworkResponse;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.RequestStatus;

import java.lang.ref.WeakReference;

public class RequestTokenTask extends AsyncTask<Void, Void, NetworkResponse<String>> {

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onSuccess(String token);

        void onFailure(String reason);
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
    protected NetworkResponse<String> doInBackground(Void... params) {
        return oAuthAuthenticator.retrieveAuthenticationUrl();
    }

    @Override
    protected void onPostExecute(NetworkResponse<String> response) {
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            if (response.getStatus() == RequestStatus.REQUEST_SUCCESS) {
                callback.onSuccess(response.getResponse());
            } else {
                callback.onFailure(response.getResponse());
            }
        }
    }
}
