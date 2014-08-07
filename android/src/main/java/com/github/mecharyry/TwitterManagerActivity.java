package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.OAuthWebViewActivity;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity {

    public static final int REQUEST_CODE = 100;
    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private OAuthAuthenticator oAuthAuthenticator;
    private AccessTokenPreferences accessTokenPreferences;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        manager = getSupportFragmentManager();
        replaceFragment(new AuthenticationFragment());

        oAuthAuthenticator = OAuthAuthenticator.newInstance();
        accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        authenticateUser();
    }

    public void authenticateUser() {
        RequestTokenTask.newInstance(requestTokenCallback, oAuthAuthenticator).execute();
    }

    private final RequestTokenTask.Callback requestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            startWebView(response);
        }

        @Override
        public void onError(String message) {

        }
    };

    public void startWebView(String response) {
        Intent intent = new Intent(this, OAuthWebViewActivity.class);
        intent.putExtra(OAuthWebViewActivity.EXTRA_REQUEST_URL, response);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);
            requestAccessToken(verifier);
        }
    }

    private void requestAccessToken(String result) {
        RequestAccessTokenTask.newInstance(accessTokenTask, oAuthAuthenticator).executeTask(result);
    }

    private final RequestAccessTokenTask.Callback accessTokenTask = new RequestAccessTokenTask.Callback() {

        @Override
        public void onRetrieved(AccessToken response) {
            accessTokenPreferences.saveAccessToken(response);
            replaceFragment(new TweetPagerFragment());
        }
    };

    private void replaceFragment(Fragment replaceWith) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, replaceWith);
        transaction.commit();
        manager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (shouldGoToFirstPage(fragment)) {
            TweetPagerFragment tweetPagerFragment = (TweetPagerFragment) fragment;
            tweetPagerFragment.moveToFirstPage();
        } else {
            super.onBackPressed();
        }
    }

    public boolean shouldGoToFirstPage(Fragment fragment) {
        return fragment instanceof TweetPagerFragment && notShowingFirstPageOf((TweetPagerFragment) fragment);
    }

    private boolean notShowingFirstPageOf(TweetPagerFragment fragment) {
        return !fragment.isViewingFirstPage();
    }
}