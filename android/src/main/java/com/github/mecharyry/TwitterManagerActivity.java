package com.github.mecharyry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements AuthenticationFragment.Callback, TweetPagerFragment.Callback {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, new AuthenticationFragment());
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAuthenticated(boolean authenticated) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, new TweetPagerFragment());
        transaction.commit();
        manager.popBackStack();
    }

    @Override
    public void onClearCredentials() {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, new AuthenticationFragment());
        transaction.commit();
        manager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if(fragment instanceof TweetPagerFragment){
            TweetPagerFragment tweetFragment = (TweetPagerFragment) fragment;
            tweetFragment.moveToFirstFragment();
        }
    }
}
