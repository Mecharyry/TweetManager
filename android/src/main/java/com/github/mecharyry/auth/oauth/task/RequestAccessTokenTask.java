package com.github.mecharyry.auth.oauth.task;

import android.os.AsyncTask;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.NetworkResponse;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.RequestStatus;

import java.lang.ref.WeakReference;

public class RequestAccessTokenTask extends AsyncTask<String, Void, NetworkResponse> {

    public static final int FIRST_INDEX = 0;
    private OAuthAuthenticator oAuthAuthenticator;
    private final WeakReference<Notify> callbackWeakReference;

    public interface Notify {
        void onSuccess(AccessToken token);

        void onFailure(String message);
    }

    public static RequestAccessTokenTask newInstance(Notify notify, OAuthAuthenticator oAuthAuthenticator) {
        WeakReference<Notify> weakReference = new WeakReference<Notify>(notify);
        return new RequestAccessTokenTask(weakReference, oAuthAuthenticator);
    }

    private RequestAccessTokenTask(WeakReference<Notify> callbackWeakReference, OAuthAuthenticator oAuthAuthenticator) {
        this.callbackWeakReference = callbackWeakReference;
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    public void executeTask(String verifier) {
        this.execute(verifier);
    }

    @Override
    protected NetworkResponse doInBackground(String... params) {
        return oAuthAuthenticator.retrieveAccessToken(params[FIRST_INDEX]);
    }

    @Override
    protected void onPostExecute(NetworkResponse response) {
        super.onPostExecute(response);
        Notify notify = callbackWeakReference.get();
        if (notify != null) {
            if (response.getStatus() == RequestStatus.REQUEST_SUCCESS) {
                notify.onSuccess(responseToAccessToken(response));
            } else {
                notify.onFailure(response.getResponse().toString());
            }
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
