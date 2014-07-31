package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Intent;

public class OAuthRequester {

    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private static final int REQUEST_CODE = 100;
    private final Activity activity;

    public interface Callback {
        void onRequesterResult(String result);
    }

    public static OAuthRequester newInstance(Activity activity) {
        return new OAuthRequester(activity);
    }

    OAuthRequester(Activity activity) {
        this.activity = activity;
    }

    public void request(String url) {
        Intent intent = new Intent(activity, OAuthRequesterActivity.class);

        intent.putExtra(OAuthRequesterActivity.EXTRA_REQUEST_URL, url);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onOAuthRequesterResult(int requestCode, int resultCode, Intent data, Callback callback) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);
            callback.onRequesterResult(verifier);
        }
    }
}
