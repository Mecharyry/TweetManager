package com.github.mecharyry;

import android.os.AsyncTask;
import android.util.Log;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class RequestAccessToken extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "RequestAccessToken";
    private final OAuthConsumer consumer;
    private final OAuthProvider provider;
    private final String oauthVerifier;

    public RequestAccessToken(OAuthConsumer consumer, OAuthProvider provider, String oauthVerifier) {
        this.consumer = consumer;
        this.provider = provider;
        this.oauthVerifier = oauthVerifier;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(TAG, "Fetching access token from Twitter...");

        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Request Token: " + consumer.getToken());
        Log.i(TAG, "Request Secret: " + consumer.getTokenSecret());
        Constants.ACCESS_TOKEN = consumer.getToken().toString();
        Constants.TOKEN_SECRET = consumer.getTokenSecret().toString();
        return null;
    }
}
