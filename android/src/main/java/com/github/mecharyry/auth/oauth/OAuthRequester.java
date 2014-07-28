package com.github.mecharyry.auth.oauth;

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;

public class OAuthRequester {
    private static final String OAUTH_VERIFIER = "OAUTH_VERIFIER";
    private static final int REQUEST_CODE = 100;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRequesterResult(String result);
    }

    public OAuthRequester(WeakReference<Callback> callbackWeakReference) {
        this.callbackWeakReference = callbackWeakReference;
    }

    public void request(Activity activity, String url) {
        Intent intent = new Intent(activity, OAuthRequesterActivity.class);
        intent.putExtra("URL", url);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onOAuthRequesterResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            String verifier = data.getStringExtra(OAUTH_VERIFIER);

            Callback callback = callbackWeakReference.get();
            if(callback != null){
                callback.onRequesterResult(verifier);
            }
        }
    }
}
