package com.github.mecharyry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class AuthenticationActivity extends Activity {

    private static final String TAG = "MainActivity";
    static final String CONSUMER_KEY = "Mz3VVcNtAX7m1UIi2tS8Xf8X9";
    static final String CONSUMER_SECRET = "Lj5tHjg5sl2OXHrfjzBI9yTc86wIs7PN4MUJPOUo7076vdciiH";
    private static final OAuthConsumer CONSUMER = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_ACCESS_SECRET = "access_secret";
    private static final String REQUEST_TOKEN_ENDPOINT_URL = "https://api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT_URL = "https://api.twitter.com/oauth/access_token";
    private static final String AUTHORIZATION_WEBSITE_URL = "https://api.twitter.com/oauth/authorize";
    private final static OAuthProvider PROVIDER = new CommonsHttpOAuthProvider(
            REQUEST_TOKEN_ENDPOINT_URL,
            ACCESS_TOKEN_ENDPOINT_URL,
            AUTHORIZATION_WEBSITE_URL
    );
    private static SharedPreferences preferences;

    private final View.OnClickListener onAuthorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasAccess()) {
                loginAuthenticatedUser(v);
            } else {
                loginNewUser(v);
            }
        }

        private void loginAuthenticatedUser(View v) {
            Log.i(TAG, "Retrieving from storage");
            CONSUMER.setTokenWithSecret(getAccessToken(), getAccessTokenSecret());
            Intent intent = new Intent(v.getContext(), ListViewActivity.class);
            intent.putExtra("CONSUMER", CONSUMER);
            startActivity(intent);
        }

        private void loginNewUser(View v) {
            Toast.makeText(AuthenticationActivity.this, "Opening Browser", Toast.LENGTH_SHORT).show();
            new RequestTokenTask(v.getContext()).execute();
        }

        private String getAccessTokenSecret() {
            return preferences.getString(PREF_ACCESS_SECRET, null);
        }

        private String getAccessToken() {
            return preferences.getString("access_token", null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.authentication_activity);

        Button button = (Button) findViewById(R.id.button_authorize);
        button.setOnClickListener(onAuthorizeButtonClicked);

        preferences = getSharedPreferences("twitter_prefs", MODE_PRIVATE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "Redirect complete.");
        handleResponse(intent);
    }

    private void handleResponse(Intent intent) {
        Uri responseUri = intent.getData();
        if (responseUri != null) {
            String oauthVerifier = responseUri.getQueryParameter("oauth_verifier");

            authoriseUser(oauthVerifier);
        }
    }

    private void authoriseUser(String oauthVerifier) {
        new AccessTokenTask(oauthVerifier).execute();
    }

    private boolean hasAccess() {
        return preferences.contains(PREF_ACCESS_TOKEN) && preferences.contains(PREF_ACCESS_SECRET);
    }

    private static class RequestTokenTask extends AsyncTask<Void, Void, Void> {
        private static final String OAUTH_CALLBACK_URL = "mecharyry-android:///";
        private static final String TAG = "RequestTokenTask";
        private final Context context;

        public RequestTokenTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "Fetching request token from Twitter...");

            try {
                String authUrl = PROVIDER.retrieveRequestToken(CONSUMER, OAUTH_CALLBACK_URL);
                Log.i(TAG, authUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
                context.startActivity(intent);
            } catch (OAuthMessageSignerException e) {
                Log.e(TAG, "OAuthMessageSignerException", e);
            } catch (OAuthNotAuthorizedException e) {
                Log.e(TAG, "OAuthNotAuthorizedException", e);
            } catch (OAuthExpectationFailedException e) {
                Log.e(TAG, "OAuthExpectationFailedException", e);
            } catch (OAuthCommunicationException e) {
                Log.e(TAG, "OAuthCommunicationException", e);
            }

            return null;
        }
    }

    private static class AccessTokenTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "AccessTokenTask";
        private final String oauthVerifier;
        private String accessToken;
        private String accessSecret;

        private AccessTokenTask(String oauthVerifier) {
            this.oauthVerifier = oauthVerifier;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "Fetching access token from Twitter...");

            try {
                PROVIDER.retrieveAccessToken(CONSUMER, oauthVerifier);
            } catch (OAuthMessageSignerException e) {
                Log.e(TAG, "OAuthMessageSignerException", e);
            } catch (OAuthNotAuthorizedException e) {
                Log.e(TAG, "OAuthNotAuthorizedException", e);
            } catch (OAuthExpectationFailedException e) {
                Log.e(TAG, "OAuthExpectationFailedException", e);
            } catch (OAuthCommunicationException e) {
                Log.e(TAG, "OAuthCommunicationException", e);
            }

            accessToken = CONSUMER.getToken() != null ? CONSUMER.getToken() : "";
            accessSecret = CONSUMER.getTokenSecret() != null ? CONSUMER.getTokenSecret() : "";
            saveAccessToken();
            return null;
        }

        private void saveAccessToken() {
            if (accessToken != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREF_ACCESS_TOKEN, accessToken);
                editor.putString(PREF_ACCESS_SECRET, accessSecret);
                editor.commit();
            }
        }
    }
}
