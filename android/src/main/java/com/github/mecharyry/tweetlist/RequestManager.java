package com.github.mecharyry.tweetlist;

import android.util.Log;

import com.github.mecharyry.tweetlist.task.PerformGetTask;

import java.io.InputStream;

public class RequestManager {
    private static final String TAG = "RequestManager";

    public static RequestManager newInstance(){
        return new RequestManager();
    }

    private final PerformGetTask.Callback performGetCallback = new PerformGetTask.Callback() {
        @Override
        public void onRetrieved(InputStream stream) {
            Log.i(TAG, "PerformGetTask Callback");
        }
    };

    public void requestAndroidDevTweets() {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
        //String signedUrl = signUrl(unsignedUrl); // TODO: Have a signer class? or expose a method in the Authenticator?
        //Log.i(TAG, "URL: " + signedUrl);
        //new PerformGetTask(performGetCallback).execute(signedUrl);
    }
}
