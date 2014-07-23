package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mecharyry.R;
import com.github.mecharyry.tweetlist.ListViewActivity;

public class AuthenticationActivity extends Activity {

    private AuthenticationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = AuthenticationManager.newInstance(this);

        setContentView(R.layout.authentication_activity);

        Button button = (Button) findViewById(R.id.button_authorize);
        button.setOnClickListener(onAuthorizeButtonClicked);

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
            Intent intent = new Intent(AuthenticationActivity.this, ListViewActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }


}
