package com.github.mecharyry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements AuthenticationFragment.Callback, TweetPagerFragment.Callback {

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
    }

    @Override
    public void onAuthenticated(boolean authenticated) {
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
        if (fragment instanceof TweetPagerFragment) {
            TweetPagerFragment tweetFragment = (TweetPagerFragment) fragment;
            if (tweetFragment.isViewingFirstPage()) {
                super.onBackPressed();
            } else {
                tweetFragment.moveToFirstPage();
            }
        } else {
            super.onBackPressed();
        }
    }
}
