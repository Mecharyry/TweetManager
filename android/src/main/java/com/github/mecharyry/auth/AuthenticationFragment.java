package com.github.mecharyry.auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mecharyry.R;

public class AuthenticationFragment extends Fragment {

    private AuthenticationManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = AuthenticationManager.newInstance(getActivity());
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        view.findViewById(R.id.button_authentication).setOnClickListener(onAuthenticationButtonClick);
        return view;
    }

    private final View.OnClickListener onAuthenticationButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            authenticateUser();
            v.findViewById(R.id.button_authentication).setEnabled(false);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(manager.hasAccessToken()){
            manager.authenticated();
        }
    }

    private void authenticateUser() {
        Toast.makeText(getActivity(), getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
        manager.authenticateUser();
    }
}
