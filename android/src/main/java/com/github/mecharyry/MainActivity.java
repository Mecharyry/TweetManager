package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

public class MainActivity extends Activity {

    private static final String API_KEY = "Dy9QbfdP4qTEOW1fcJRagEkC8";
    private static final String API_SECRET = "YXMPA1l2k18J3dmM4u4ta892K7QspnKnBFf352P2dM8NIIQlaX";
    final OAuthProvider provider;
    private final OAuthConsumer consumer;
    private final ToastValidator validator;
    private final View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validator.canToast()) {
                Toast.makeText(MainActivity.this, "Lets Have Toast!", Toast.LENGTH_SHORT).show();
                new RequestTokenTask(v.getContext(), consumer, provider).execute();
            }
        }
    };

    public MainActivity() {
        String requestTokenEndpointUrl = "https://api.twitter.com/oauth/request_token";
        String accessTokenEndpointUrl = "https://api.twitter.com/oauth/access_token";
        String authorizationWebsiteUrl = "https://api.twitter.com/oauth/authorize";

        consumer = new CommonsHttpOAuthConsumer(API_KEY, API_SECRET);
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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(onButtonClicked);
    }
}
