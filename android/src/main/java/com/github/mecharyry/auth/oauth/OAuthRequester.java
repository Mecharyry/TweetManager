package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class OAuthRequester {

    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private static final int REQUEST_CODE = 100;
    private final Fragment fragment;

    public interface Callback {
        void onRequesterResult(String result);
    }

    public static OAuthRequester newInstance(Fragment fragment) {
        return new OAuthRequester(fragment);
    }

    OAuthRequester(Fragment fragment) {
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
