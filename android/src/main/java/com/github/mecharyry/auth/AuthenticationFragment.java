package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.BuildConfig;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

public class AuthenticationFragment extends Fragment {

    public static final String NOT_IMPLEMENTED_MESSAGE = " must implement Callback.";
    private OAuthAuthenticator oAuthAuthenticator;
    private AccessTokenPreferences accessTokenPreferences;
    private BroadcastReceiver receiver;
    private Button authenticationButton;
    private TextView textView;
    private Callback callback;

    public interface Callback {
        void onRequestTokenResponse(String url);

        void onAuthenticated();
    }

    public AuthenticationFragment() {
        oAuthAuthenticator = OAuthAuthenticator.newInstance(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        accessTokenPreferences = AccessTokenPreferences.newInstance(activity);
        receiver = new NetworkChangeReceiver(onNetworkChange);
        activity.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getName() + NOT_IMPLEMENTED_MESSAGE);
        }

        if (accessTokenPreferences.hasAccess()) {
            callback.onAuthenticated();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        authenticationButton = (Button) view.findViewById(R.id.button_authentication);
        authenticationButton.setOnClickListener(onAuthenticationButtonClick);
        textView = (TextView) view.findViewById(R.id.text_warning_message);
        return view;
    }

    private final View.OnClickListener onAuthenticationButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
            requestAuthUrl();
        }
    };

    private void requestAuthUrl() {
        authenticationButton.setEnabled(false);
        RequestTokenTask.newInstance(onRequestTokenCallback, oAuthAuthenticator).execute();
    }

    private final RequestTokenTask.Callback onRequestTokenCallback = new RequestTokenTask.Callback() {
        @Override
        public void onSuccess(String response) {
            callback.onRequestTokenResponse(response);
        }

        @Override
        public void onFailure(String message) {
            setErrorMessage(message);
            authenticationButton.setEnabled(NetworkChangeReceiver.isNetworkAvailable(getActivity()));
        }
    };

    public void requestAccessToken(String verifier) {
        RequestAccessTokenTask.newInstance(onRequestAccessTokenCallback, oAuthAuthenticator).executeTask(verifier);
    }

    private final RequestAccessTokenTask.Callback onRequestAccessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onSuccess(AccessToken token) {
            accessTokenPreferences.saveAccessToken(token);
            callback.onAuthenticated();
        }

        @Override
        public void onFailure(String message) {
            setErrorMessage(message);
            authenticationButton.setEnabled(NetworkChangeReceiver.isNetworkAvailable(getActivity()));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private final NetworkChangeReceiver.Callback onNetworkChange = new NetworkChangeReceiver.Callback() {
        @Override
        public void networkAvailable() {
            authenticationButton.setEnabled(true);
        }

        @Override
        public void networkUnavailable() {
            authenticationButton.setEnabled(false);
        }
    };

    private void setErrorMessage(String message) {
        textView.setText(message);
    }
}
