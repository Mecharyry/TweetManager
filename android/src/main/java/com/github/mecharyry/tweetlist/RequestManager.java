package com.github.mecharyry.tweetlist;

import android.util.Log;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.PerformGetTask;
import com.github.mecharyry.tweetlist.task.PerformJsonParsingTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class RequestManager {
    private static final String TAG = "RequestManager";
    private static OAuthConsumer oAuthConsumer;
    private OAuthConsumer consumer;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onResponseResult(List<Tweet> tweets);
    }

    private final PerformJsonParsingTask.Callback onResponse = new PerformJsonParsingTask.Callback() {
        @Override
        public void jsonParsed(List<Tweet> tweets) {
            Callback callback = callbackWeakReference.get();
            if (callback != null) {
                callback.onResponseResult(tweets);
            }
        }
    };

    public static RequestManager newInstance(AccessToken accessToken, Callback callback) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        oAuthConsumer = oAuthAuthenticator.getConsumer(accessToken);
        return new RequestManager(oAuthConsumer, callback);
    }

    public RequestManager(OAuthConsumer consumer, Callback callback) {
        this.consumer = consumer;
        callbackWeakReference = new WeakReference<Callback>(callback);
    }

    private final PerformGetTask.Callback onPerformGetResponse = new PerformGetTask.Callback() {
        @Override
        public void onRetrieved(String response) {
            Log.i(TAG, "PerformGetTask Callback");
            JSONObject jsonObject = convertStringToJson(response);
            new PerformJsonParsingTask(onResponse, jsonObject).execute();   // TODO: Callback to the listview;
        }
    };

    public void requestAndroidDevTweets() {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50"; // TODO: Question: Could we attach this to a particular button rather than having it in code?
        String signedUrl = signUrl(unsignedUrl);
        new PerformGetTask(onPerformGetResponse).execute(signedUrl);
    }

    private String signUrl(String unsignedUrl) {
        String signedUrl = null;
        try {
            signedUrl = consumer.sign(unsignedUrl);
        } catch (OAuthMessageSignerException e) {
            throwAuthException(e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException(e);
        } catch (OAuthCommunicationException e) {
            throwAuthException(e);
        }
        return signedUrl;
    }

    protected static JSONObject convertStringToJson(String input) { // TODO: Move to new class.
        try {
            Log.i(TAG, new JSONObject(input).toString());
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

    private void throwAuthException(Exception e) {
        throw new OAuthAuthenticator.OAuthException(e);
    }
}
