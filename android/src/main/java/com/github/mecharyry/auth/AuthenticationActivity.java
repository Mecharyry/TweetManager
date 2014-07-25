package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.tweetlist.AndroidDevTweetsActivity;

public class AuthenticationActivity extends Activity {

    private AuthenticationManager manager;
    private AccessTokenPreferences accessTokenPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = AuthenticationManager.newInstance(this);
        accessTokenPreferences = AccessTokenPreferences.newInstance(this.getApplicationContext());

        setContentView(R.layout.authentication_activity);
        findViewById(R.id.button_authorize).setOnClickListener(onAuthorizeButtonClicked);
        findViewById(R.id.button_clear_credentials).setOnClickListener(onClearCredentialsClicked);
    }

    private final View.OnClickListener onAuthorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (manager.hasAccessToken()) {
                navigateToTweetList();
            } else {
                Toast.makeText(AuthenticationActivity.this, "Opening Browser", Toast.LENGTH_SHORT).show();
                manager.authenticate();
            }
        }

        private void navigateToTweetList() {
            Intent intent = new Intent(AuthenticationActivity.this, AndroidDevTweetsActivity.class);
            startActivity(intent);
        }
    };



    private final View.OnClickListener onClearCredentialsClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            accessTokenPreferences.removeAccessToken();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }


}
