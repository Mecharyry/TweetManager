package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mecharyry.R;

public class AuthenticationActivity extends Activity {

    private static final String TAG = "AuthenticationActivity";
    private static final String ANDROID_DEV_TWEETS_INTENT = "com.github.mecharyry.ANDROID_TWEETS_INTENT";
    private AuthenticationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);

        manager = AuthenticationManager.newInstance(this, onAccessTokenSaved);
        findViewById(R.id.button_authorize).setOnClickListener(onAuthorizeButtonClicked);
        findViewById(R.id.button_android_dev_tweets).setOnClickListener(onAndroidDevTweetsButtonClicked);
    }

    private final AuthenticationManager.Callback onAccessTokenSaved = new AuthenticationManager.Callback() {

        @Override
        public void onAuthenticated() {
            setButtonsEnabled(manager.hasAccessToken());
        }
    };

    private final View.OnClickListener onAuthorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(AuthenticationActivity.this, getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
            manager.authenticate();
        }
    };

    private final View.OnClickListener onAndroidDevTweetsButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ANDROID_DEV_TWEETS_INTENT);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authentication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear_credentials:
                manager.removeAccessToken();
                setButtonsEnabled(manager.hasAccessToken());
                break;
            default:
                Log.e(TAG, "Menu item not handled.");

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }

    private void setButtonsEnabled(boolean hasAccess) {
        findViewById(R.id.button_android_dev_tweets).setEnabled(hasAccess);
        findViewById(R.id.button_my_stream).setEnabled(hasAccess);
    }
}
