package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mecharyry.oauth.AccessToken;
import com.github.mecharyry.oauth.OAuthAuthenticator;
import com.github.mecharyry.oauth.OAuthRequesterActivity;
import com.github.mecharyry.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.oauth.task.RequestTokenTask;

public class AuthenticationActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_ACCESS_SECRET = "access_secret";
    private static SharedPreferences preferences;
    private final OAuthAuthenticator oAuthAuthentication;

    public AuthenticationActivity() {
        oAuthAuthentication = OAuthAuthenticator.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.authentication_activity);

        Button button = (Button) findViewById(R.id.button_authorize);
        button.setOnClickListener(onAuthorizeButtonClicked);

        preferences = getSharedPreferences("twitter_prefs", MODE_PRIVATE);
    }

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

        }

        private void loginNewUser(View v) {
            Toast.makeText(AuthenticationActivity.this, "Opening Browser", Toast.LENGTH_SHORT).show();
            new RequestTokenTask(requestTokenCallback, oAuthAuthentication).execute();
        }

    };

    RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            Intent intent = new Intent(AuthenticationActivity.this, OAuthRequesterActivity.class);
            intent.putExtra("URL", response);
            startActivityForResult(intent, 100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            String verifier = data.getStringExtra("OAUTH_VERIFIER");
            new RequestAccessTokenTask(accessTokenCallback, oAuthAuthentication).execute(verifier);
        }
    }

    private final RequestAccessTokenTask.Callback accessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_ACCESS_TOKEN, response.getToken());
            editor.putString(PREF_ACCESS_SECRET, response.getSecret());
            editor.apply();
        }
    };

    private boolean hasAccess() {
        return preferences.contains(PREF_ACCESS_TOKEN) && preferences.contains(PREF_ACCESS_SECRET);
    }


}
