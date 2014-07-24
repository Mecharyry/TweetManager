package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PerformJsonParsingTask extends AsyncTask<Void, Void, List<Tweet>> {

    private static final String TAG = "PerformJsonParsingTask";
    private static final String TAG_STATUSES = "statuses";
    private static final String TAG_TEXT = "text";
    private static final String TAG_USER = "user";
    private static final String TAG_SCREEN_NAME = "screen_name";
    private static final String TAG_LOCATION = "location";
    private final JSONObject jsonObject;
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void jsonParsed(List<Tweet> tweets);
    }

    public PerformJsonParsingTask(Callback callback, JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        callbackWeakReference = new WeakReference<Callback>(callback);
    }

    @Override
    protected List<Tweet> doInBackground(Void... params) {
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray statuses = jsonObject.getJSONArray(TAG_STATUSES);

            for (int i = 0; i < statuses.length(); i++) {
                JSONObject jsonObject = statuses.getJSONObject(i);
                JSONObject user = jsonObject.getJSONObject(TAG_USER);
                String screenName = user.getString(TAG_SCREEN_NAME);
                String location = user.getString(TAG_LOCATION);
                String text = jsonObject.getString(TAG_TEXT);

                Tweet tweet = new Tweet(screenName, location, text);

                tweets.add(tweet);
            }
            return tweets;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Tweet> tweets) {
        super.onPostExecute(tweets);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.jsonParsed(tweets);
        }
    }
}
