package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Intent;

public class OAuthRequester {
    private static final int REQUEST_CODE = 100;
    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private final AuthenticatorRequesterResult onResult;

    public interface AuthenticatorRequesterResult {
        void onRequesterResult(String result);
    }

    public OAuthRequester(AuthenticatorRequesterResult onResult) {
        this.onResult = onResult;
    }

    public void request(Activity activity, String url) {
        Intent intent = new Intent(activity, OAuthRequesterActivity.class);
        intent.putExtra("URL", url);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onOAuthRequesterResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);
            onResult.onRequesterResult(verifier);
        }
    }
}
