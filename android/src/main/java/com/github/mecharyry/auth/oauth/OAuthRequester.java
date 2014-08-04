package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Intent;

import com.github.mecharyry.auth.AuthenticationFragment;

public class OAuthRequester {

    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private static final int REQUEST_CODE = 100;
    private final AuthenticationFragment fragment;

    public interface Callback {
        void onRequesterResult(String result);
    }

    public static OAuthRequester newInstance(AuthenticationFragment fragment) {
        return new OAuthRequester(fragment);
    }

    OAuthRequester(AuthenticationFragment fragment) {
        this.fragment = fragment;
    }

    public void request(String url) {
        Intent intent = new Intent(fragment.getActivity(), OAuthRequesterActivity.class);

        intent.putExtra(OAuthRequesterActivity.EXTRA_REQUEST_URL, url);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onOAuthRequesterResult(int requestCode, int resultCode, Intent data, Callback callback) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);
            callback.onRequesterResult(verifier);
        }
    }
}
