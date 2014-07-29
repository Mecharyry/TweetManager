package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;

import com.github.mecharyry.tweetlist.JsonParsing;
import com.github.mecharyry.tweetlist.TwitterRequester;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class PerformGetTask extends AsyncTask<String, Void, List<Tweet>> {

    private final WeakReference<Callback> callbackWeakReference;
    private final TwitterRequester twitterRequester;
    private final JsonParsing jsonParsing;

    public interface Callback {
        void onGetResponse(List<Tweet> tweets);
    }

    public static PerformGetTask newInstance(Callback callback) {
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        TwitterRequester twitterRequester = new TwitterRequester();
        JsonParsing jsonParsing = JsonParsing.newInstance();
        return new PerformGetTask(callbackWeakReference, twitterRequester, jsonParsing);
    }

    private PerformGetTask(WeakReference<Callback> callbackWeakReference, TwitterRequester twitterRequester, JsonParsing jsonParsing) {
        this.callbackWeakReference = callbackWeakReference;
        this.twitterRequester = twitterRequester;
        this.jsonParsing = jsonParsing;
    }

    @Override
    protected List<Tweet> doInBackground(String... urls) {
        JSONObject jsonObject = twitterRequester.request(urls[0]);
        return jsonParsing.TweetsByHashTag(jsonObject);
    }

    @Override
    protected void onPostExecute(List<Tweet> response) {
        super.onPostExecute(response);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onGetResponse(response);
        }
    }

    public void executeTask(String url){
        this.execute(url);
    }
}
