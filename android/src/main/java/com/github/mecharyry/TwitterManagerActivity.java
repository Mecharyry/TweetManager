package com.github.mecharyry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.oauth.OAuthRequester;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements AuthenticationFragment.Callback, TweetPagerFragment.Callback, OAuthRequester.ActivityCallback {

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
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAuthenticated() {
        replaceFragment(new TweetPagerFragment());
    }

    @Override
    public void onClearCredentials() {
        replaceFragment(new AuthenticationFragment());
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
    public void startActivity(Intent intent) {
        startActivityForResult(intent, OAuthRequester.REQUEST_CODE);
    }
}