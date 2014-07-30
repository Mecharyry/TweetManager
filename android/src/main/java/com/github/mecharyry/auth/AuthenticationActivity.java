package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mecharyry.R;
import com.github.mecharyry.tweetlist.AndroidDevTweetsActivity;
import com.github.mecharyry.tweetlist.MyStreamActivity;

public class AuthenticationActivity extends Activity {

    private static final String TAG = AuthenticationActivity.class.getSimpleName();
    private static final String MENU_ITEM_EXCEPTION = TAG + ": Menu item not handled.";
    private AuthenticationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);

        manager = AuthenticationManager.newInstance(this, onAccessTokenSaved);
        findViewById(R.id.button_authorize).setOnClickListener(onAuthorizeButtonClick);
        findViewById(R.id.button_android_dev_tweets).setOnClickListener(onAndroidDevTweetsButtonClick);
        findViewById(R.id.button_my_stream).setOnClickListener(onMyStreamTweetsButtonClick);
    }

    private final AuthenticationManager.Callback onAccessTokenSaved = new AuthenticationManager.Callback() {

        @Override
        public void onAuthenticated() {
            setButtonsEnabled(manager.hasAccessToken());
        }
    };

    private final View.OnClickListener onAuthorizeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(AuthenticationActivity.this, getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
            manager.authenticate();
        }
    };

    private final View.OnClickListener onAndroidDevTweetsButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            createShowDevTweetsIntent();
        }

        private void createShowDevTweetsIntent() {
            Intent intent = new Intent(AndroidDevTweetsActivity.ANDROID_DEV_TWEETS_INTENT);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onMyStreamTweetsButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            createShowMyStreamIntent();
        }

        private void createShowMyStreamIntent() {
            Intent intent = new Intent(MyStreamActivity.MY_STREAM_TWEETS_INTENT);
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
                throw new RuntimeException(MENU_ITEM_EXCEPTION);

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
