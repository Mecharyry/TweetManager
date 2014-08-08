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
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.auth.oauth.task.RequestAccessTokenTask;
import com.github.mecharyry.auth.oauth.task.RequestTokenTask;

public class AuthenticationFragment extends Fragment {

    private OAuthAuthenticator oAuthAuthenticator;
    private AccessTokenPreferences accessTokenPreferences;
    private BroadcastReceiver receiver;
    private Button authenticationButton;
    private TextView textView;
    private NotifyActivity notifyActivity;

    public interface NotifyActivity {
        void startWebView(String url);

        void onAuthenticated();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        oAuthAuthenticator = OAuthAuthenticator.newInstance();
        accessTokenPreferences = AccessTokenPreferences.newInstance(activity);
        receiver = new NetworkChangeReceiver(connectionChangedReceiver);
        activity.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        try {
            notifyActivity = (NotifyActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callback.");
        }

        if (accessTokenPreferences.hasAccess()) {
            notifyActivity.onAuthenticated();
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
        RequestTokenTask.newInstance(requestTokenCallback, oAuthAuthenticator).execute();
    }

    private final RequestTokenTask.Notify requestTokenCallback = new RequestTokenTask.Notify() {
        @Override
        public void onRetrieved(String response) {
            notifyActivity.startWebView(response);
        }

        @Override
        public void onError(String message) {
            setErrorMessage(message);
            authenticationButton.setEnabled(NetworkChangeReceiver.isNetworkAvailable(getActivity()));
        }
    };

    public void requestAccessToken(String verifier) {
        RequestAccessTokenTask.newInstance(requestAccessTokenCallback, oAuthAuthenticator).executeTask(verifier);
    }

    private final RequestAccessTokenTask.Callback requestAccessTokenCallback = new RequestAccessTokenTask.Callback() {
        @Override
        public void onRetrieved(AccessToken response) {
            accessTokenPreferences.saveAccessToken(response);
            notifyActivity.onAuthenticated();
        }

        @Override
        public void onError(String message) {
            setErrorMessage(message);
            authenticationButton.setEnabled(NetworkChangeReceiver.isNetworkAvailable(getActivity()));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private final NetworkChangeReceiver.Callback connectionChangedReceiver = new NetworkChangeReceiver.Callback() {
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
