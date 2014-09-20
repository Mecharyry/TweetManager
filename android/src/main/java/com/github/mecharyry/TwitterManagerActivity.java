package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.oauth.OAuthWebViewActivity;
import com.github.mecharyry.tweetlist.TabVisibilityController;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements AuthenticationFragment.Callback, TweetPagerFragment.Callback, TabVisibilityController {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            replaceFragment(new AuthenticationFragment());
        }
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
        } else {
            throw DeveloperError.because("Authentication fragment missing.", new ClassNotFoundException());
        }
    }

    private void replaceFragment(Fragment replaceWith) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, replaceWith);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (shouldGoToFirstPage(fragment)) {
            goToFirstPage((TweetPagerFragment) fragment);
        } else {
            super.onBackPressed();
        }
    }

    private void goToFirstPage(TweetPagerFragment fragment) {
        fragment.moveToFirstPage();
    }

    public boolean shouldGoToFirstPage(Fragment fragment) {
        return fragment instanceof TweetPagerFragment && notShowingFirstPageOf((TweetPagerFragment) fragment);
    }

    private boolean notShowingFirstPageOf(TweetPagerFragment fragment) {
        return !fragment.isViewingFirstPage();
    }

    @Override
    public void onRequestTokenResponse(String url) {
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

    @Override
    public void onShowTabs() {
        findViewById(R.id.pager_tab_strip).setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideTabs() {
        findViewById(R.id.pager_tab_strip).setVisibility(View.GONE);
    }
}
