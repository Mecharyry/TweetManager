package com.github.mecharyry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.AuthenticationManager;
import com.github.mecharyry.tweetlist.TweetPagerFragment;

public class TwitterManagerActivity extends FragmentActivity implements TweetPagerFragment.Callback, AuthenticationManager.NotifyActivity {

    private FragmentManager manager;
    private Callback callback;

    public interface Callback {
        void onWebViewResponse(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        manager = getSupportFragmentManager();
        replaceFragment(new AuthenticationFragment());
    }

    @Override
    public void startWebView(Intent intent, Callback callback) {
        startActivityForResult(intent, AuthenticationManager.REQUEST_CODE);
        this.callback = callback;
    }

    @Override
    public void onError(String message) {
        // TODO: Reset GUI and notify user an error occurred.
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if(fragment instanceof  AuthenticationFragment){
            ((AuthenticationFragment) fragment).displayErrorMessage(message);
        }
    }

    @Override
    public void onAuthenticated() {
        replaceFragment(new TweetPagerFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback != null) {
            callback.onWebViewResponse(requestCode, resultCode, data);
        }
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
}