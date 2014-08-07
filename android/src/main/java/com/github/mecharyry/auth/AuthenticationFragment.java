package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mecharyry.R;

public class AuthenticationFragment extends Fragment {

    //private AuthenticationManager manager;
    private BroadcastReceiver receiver;
    private Button authenticationButton;
    private TextView textView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        receiver = new NetworkChangeReceiver(connectionChangedReceiver);
        activity.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //manager = AuthenticationManager.newInstance(getActivity());
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        authenticationButton = (Button) view.findViewById(R.id.button_authentication);
        authenticationButton.setOnClickListener(onAuthenticationButtonClick);
        textView = (TextView) view.findViewById(R.id.text_warning_message);
        return view;
    }

    private final View.OnClickListener onAuthenticationButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            authenticateUser();
        }
    };

    private void authenticateUser() {
        Toast.makeText(getActivity(), getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
        //manager.authenticateUser();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (manager.hasAccessToken()) {
//            manager.authenticated();
//        }
    }

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

    public void displayErrorMessage(String message){
        textView.setText(message);
    }
}
