package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.NetworkResponse;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.RequestStatus;

import java.lang.ref.WeakReference;

public class RequestTokenTask extends AsyncTask<Void, Void, NetworkResponse> {

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Notify> callbackWeakReference;

    public interface Notify {
        void onRetrieved(String response);

        void onError(String message);
    }

    public static RequestTokenTask newInstance(Notify notify, OAuthAuthenticator oAuthAuthenticator) {
        WeakReference<Notify> weakReference = new WeakReference<Notify>(notify);
        return new RequestTokenTask(weakReference, oAuthAuthenticator);
    }

    private RequestTokenTask(WeakReference<Notify> callbackWeakReference, OAuthAuthenticator oAuthAuthenticator) {
        this.callbackWeakReference = callbackWeakReference;
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    @Override
    protected NetworkResponse doInBackground(Void... params) {
        return oAuthAuthenticator.retrieveAuthenticationUrl();
    }

    @Override
    protected void onPostExecute(NetworkResponse response) {
        Notify notify = callbackWeakReference.get();
        if (notify != null) {
            if (response.getStatus() == RequestStatus.REQUEST_SUCCESS) {
                notify.onRetrieved(response.getResponse().toString());
            } else {
                notify.onError(response.getResponse().toString());
            }
        }
    }
}
