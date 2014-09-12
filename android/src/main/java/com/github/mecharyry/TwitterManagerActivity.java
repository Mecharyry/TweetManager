package com.github.mecharyry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.github.mecharyry.auth.AuthenticationFragment;
import com.github.mecharyry.auth.oauth.OAuthWebViewActivity;
import com.github.mecharyry.tweetlist.TabVisibilityController;
import com.github.mecharyry.tweetlist.TweetPagerFragment;
import com.leanplum.Leanplum;
import com.leanplum.activities.LeanplumFragmentActivity;

public class TwitterManagerActivity extends LeanplumFragmentActivity implements AuthenticationFragment.Callback, TweetPagerFragment.Callback, TabVisibilityController {

    public static final String APP_ID_KEY = "JgJNu8zPCsxuhp91zhH68gRLhKSqzgenHGwQbKAq7FE";
    public static final String DEV_ACCESS_KEY = "iW6U8abyB4689kwbWootwcef9WHJLe2wp0NiUglTwlQ";
    public static final String APP_PRODUCTION_KEY = "NR1XYZ2LZvf9Y2atkyyf2oOfOAr794urITafhkHm3Cw";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_manager);

        addLeanplumKeys();

        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            replaceFragment(new AuthenticationFragment());
        }
    }

    private void addLeanplumKeys() {
        if (BuildConfig.DEBUG) {
            Leanplum.setAppIdForDevelopmentMode(APP_ID_KEY, DEV_ACCESS_KEY);
        } else {
            Leanplum.setAppIdForProductionMode(APP_ID_KEY, APP_PRODUCTION_KEY);
        }
        Leanplum.start(this);
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
