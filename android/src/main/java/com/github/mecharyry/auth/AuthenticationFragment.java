package com.github.mecharyry.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mecharyry.R;
import com.github.mecharyry.tweetlist.AndroidDevTweetsFragment;
import com.github.mecharyry.tweetlist.MyStreamFragment;
import com.github.mecharyry.tweetlist.TweetPagerActivity;

public class AuthenticationFragment extends Fragment {

    private static final String TAG = AuthenticationFragment.class.getSimpleName();
    private static final String MENU_ITEM_EXCEPTION = TAG + ": Menu item not handled.";
    private AuthenticationManager manager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = AuthenticationManager.newInstance(this, onAccessTokenSaved);
        view = inflater.inflate(R.layout.authentication_menu, container, false);

        view.findViewById(R.id.button_authorize).setOnClickListener(onAuthorizeButtonClick);
        view.findViewById(R.id.button_android_dev_tweets).setOnClickListener(onAndroidDevTweetsButtonClick);
        view.findViewById(R.id.button_my_stream).setOnClickListener(onMyStreamTweetsButtonClick);

        return view;
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
            // TODO: Toast.makeText(getActivity(), getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
            // TODO: manager.authenticate();
            Intent intent = new Intent(getActivity(), TweetPagerActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onAndroidDevTweetsButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            createShowDevTweetsIntent();
        }

        private void createShowDevTweetsIntent() {
            Intent intent = new Intent(AndroidDevTweetsFragment.ACTION_VIEW_ANDROID_DEV_TWEETS);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onMyStreamTweetsButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            createShowMyStreamIntent();
        }

        private void createShowMyStreamIntent() {
            Intent intent = new Intent(MyStreamFragment.ACTION_VIEW_MY_STREAM_TWEETS);
            startActivity(intent);
        }
    };

    private void setButtonsEnabled(boolean hasAccess) {
        view.findViewById(R.id.button_android_dev_tweets).setEnabled(hasAccess);
        view.findViewById(R.id.button_my_stream).setEnabled(hasAccess);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.authentication, menu);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }
}
