package com.github.mecharyry;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class RequestTokenTask extends AsyncTask<Void, Void, Void> {

    public static final String OAUTH_CALLBACK_URL = "mecharyry-android:///";
    private static final String TAG = "RequestTokenTask";
    private final OAuthConsumer consumer;
    private final OAuthProvider provider;
    private final Context context;

    public RequestTokenTask(Context context, OAuthConsumer consumer, OAuthProvider provider) {
        this.context = context;
        this.consumer = consumer;
        this.provider = provider;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.d(TAG, "Fetching request token from Twitter...");

        try {
            String authUrl = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
            context.startActivity(intent);
        } catch (OAuthMessageSignerException e) {   // TODO Handle Exceptions
            e.printStackTrace();
            e.getMessage();
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();
            e.getMessage();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
            e.getMessage();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
            e.getMessage();
        }

        Log.d(TAG, "Request Token: " + consumer.getToken());
        Log.d(TAG, "Request Secret: " + consumer.getTokenSecret());
        return null;
    }
}
