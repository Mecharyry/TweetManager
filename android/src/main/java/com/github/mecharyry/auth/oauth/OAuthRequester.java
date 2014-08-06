package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class OAuthRequester {

    public static final int REQUEST_CODE = 100;
    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";

    public interface Callback {
        void onRequesterResult(String result);
    }

    public interface ActivityCallback {
        void startActivity(Intent intent);
    }

    public void createRequest(Context context, String url) {
        Intent intent = new Intent(context, OAuthWebViewActivity.class);
        intent.putExtra(OAuthWebViewActivity.EXTRA_REQUEST_URL, url);

        try {
            ActivityCallback callback = (ActivityCallback) context;
            callback.startActivity(intent);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Callback.");
        }
    }

    public void onOAuthRequesterResult(int requestCode, int resultCode, Intent data, Callback callback) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);
            callback.onRequesterResult(verifier);
        }
    }
}