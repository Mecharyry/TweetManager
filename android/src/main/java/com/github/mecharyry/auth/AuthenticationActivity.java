package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mecharyry.tweetlist.ListViewActivity;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;

public class AuthenticationActivity extends Activity {

    private static final String TAG = "MainActivity";
    private AuthenticationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("twitter_prefs", MODE_PRIVATE);
        manager = new AuthenticationManager(OAuthAuthenticator.newInstance(), this, preferences);

        setContentView(R.layout.authentication_activity);

        Button button = (Button) findViewById(R.id.button_authorize);
        button.setOnClickListener(onAuthorizeButtonClicked);

    }

    private final View.OnClickListener onAuthorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (manager.hasAccess()) {
                Intent intent = new Intent(AuthenticationActivity.this, ListViewActivity.class);
                startActivity(intent);
            } else {
                manager.authenticate();
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }


}
