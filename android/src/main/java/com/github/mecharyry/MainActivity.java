package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    final OAuthProvider provider;
    private final OAuthConsumer consumer;
    private final ToastValidator validator;
    private final View.OnClickListener onAuthorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validator.canToast()) {
                Toast.makeText(MainActivity.this, "Opening Browser", Toast.LENGTH_SHORT).show();
                new RequestTokenTask(v.getContext(), consumer, provider).execute();
            }
        }
    };
    private final View.OnClickListener onDevTweetsButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (Constants.ACCESS_TOKEN != null && Constants.TOKEN_SECRET != null) {
                Log.i(TAG, "Verification Successful");

                consumer.setTokenWithSecret(Constants.ACCESS_TOKEN, Constants.TOKEN_SECRET);

                AsyncHttpClient client = new AsyncHttpClient();
                String signedUrl = null;
                try {
                    signedUrl = consumer.sign("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=carl");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestParams params = new RequestParams();
                params.add("screen_name", "carl");

                Log.i(TAG, "Signed URL: " + signedUrl);

                if (signedUrl != null) {
                    client.get(signedUrl, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.i(TAG, "Call to retrieve timeline succeeded: JSONObject");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.i(TAG, "Call to retrieve timeline succeeded: JSONArray.");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Log.i(TAG, "Retrieval Failed: JSONObject: " + errorResponse.toString());
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            Log.i(TAG, "On Finish.");
                        }

                        @Override
                        public void onCancel() {
                            super.onCancel();
                            Log.i(TAG, "On Cancel.");
                        }
                    });
                }
            }
        }
    };

    public MainActivity() {
        String requestTokenEndpointUrl = "https://api.twitter.com/oauth/request_token";
        String accessTokenEndpointUrl = "https://api.twitter.com/oauth/access_token";
        String authorizationWebsiteUrl = "https://api.twitter.com/oauth/authorize";

        consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        provider = new CommonsHttpOAuthProvider(
                requestTokenEndpointUrl,
                accessTokenEndpointUrl,
                authorizationWebsiteUrl
        );

        validator = new ToastValidator();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Button button = (Button) findViewById(R.id.button_authorize);
        button.setOnClickListener(onAuthorizeButtonClicked);

        button = (Button) findViewById(R.id.buttonDevTweets);
        button.setOnClickListener(onDevTweetsButtonClicked);
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
        new RequestAccessToken(consumer, provider, oauthVerifier).execute();
    }
}
