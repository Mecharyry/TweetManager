package com.github.mecharyry.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mecharyry.R;

public class AuthenticationFragment extends Fragment {

    private static final String TAG = AuthenticationFragment.class.getSimpleName();
    private static final String MENU_ITEM_EXCEPTION = TAG + ": Menu item not handled.";
    private AuthenticationManager manager;
    private View view;
    private Callback callback;

    public interface Callback {
        void onAuthenticated(boolean authenticated);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callback.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = AuthenticationManager.newInstance(this, onAccessTokenSaved);
        view = inflater.inflate(R.layout.fragment_authentication, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (manager.hasAccessToken()) {
            onAccessTokenSaved.onAuthenticated();
        } else {
            authenticateUser();
        }
    }

    private void authenticateUser() {
        Toast.makeText(getActivity(), getString(R.string.toast_notification), Toast.LENGTH_SHORT).show();
        manager.authenticate();
    }

    private final AuthenticationManager.Callback onAccessTokenSaved = new AuthenticationManager.Callback() {

        @Override
        public void onAuthenticated() {
            callback.onAuthenticated(true);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.authentication, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear_credentials:
                manager.removeAccessToken();
                // TODO: Move to the viewPager fragment. Navigates back to authentication menu.
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
