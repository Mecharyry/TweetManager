package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.oauth.OAuthWebViewActivity;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements AuthenticationFragment.NotifyActivity, TweetPagerFragment.Callback {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        manager = getSupportFragmentManager();
        replaceFragment(new AuthenticationFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == OAuthWebViewActivity.REQUEST_CODE) {
            String verifier = data.getStringExtra(OAuthWebViewActivity.OAUTH_VERIFIER);
            requestAccessToken(verifier);
        }
    }

    private void requestAccessToken(String verifier) {
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment instanceof AuthenticationFragment) {
            ((AuthenticationFragment) fragment).requestAccessToken(verifier);
        }
    }


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

    @Override
    public void startWebView(String url) {
        Intent intent = new Intent(this, OAuthWebViewActivity.class);
        intent.putExtra(OAuthWebViewActivity.EXTRA_REQUEST_URL, url);
        startActivityForResult(intent, OAuthWebViewActivity.REQUEST_CODE);
    }

    @Override
    public void onAuthenticated() {
        replaceFragment(new TweetPagerFragment());
    }

    @Override
    public void onClearCredentials() {
        replaceFragment(new AuthenticationFragment());
    }
}