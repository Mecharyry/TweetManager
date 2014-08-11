package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.NetworkResponse;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.RequestStatus;

import java.lang.ref.WeakReference;

public class RequestTokenTask extends AsyncTask<Void, Void, NetworkResponse<String>> {

    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Notify> callbackWeakReference;

    public interface Notify {
        void onSuccess(String token);

        void onFailure(String reason);
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
    protected NetworkResponse<String> doInBackground(Void... params) {
        return oAuthAuthenticator.retrieveAuthenticationUrl();
    }

    @Override
    protected void onPostExecute(NetworkResponse<String> response) {
        Notify notify = callbackWeakReference.get();
        if (notify != null) {
            if (response.getStatus() == RequestStatus.REQUEST_SUCCESS) {
                notify.onSuccess(response.getResponse());
            } else {
                notify.onFailure(response.getResponse());
            }
        }
    }
}
