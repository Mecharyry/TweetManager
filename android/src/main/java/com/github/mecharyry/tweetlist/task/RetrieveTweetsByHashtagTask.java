package com.github.mecharyry.tweetlist.task;

import android.util.Log;

import com.github.mecharyry.ListViewActivity;
import com.github.mecharyry.tweetlist.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RetrieveTweetsByHashtagTask extends TwitterRequestTask {
    private static final String TAG = "TwitterRequest";
    private static final String TAG_STATUSES = "statuses";
    private static final String TAG_TEXT = "text";
    private static final String TAG_USER = "user";
    private static final String TAG_SCREEN_NAME = "screen_name";
    private static final String TAG_LOCATION = "location";
    private final ListViewActivity context;

    public RetrieveTweetsByHashtagTask(ListViewActivity context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {
        InputStream inputStream = super.performGet(params);

        if (inputStream != null) {
            try {
                String inputStreamString = super.inputStreamToString(inputStream);
                JSONObject jsonObject = super.convertStringToJson(inputStreamString);

                if (jsonObject != null) {
                    return jsonToTweet(jsonObject);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<?> objects) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTweets((ArrayList<Tweet>) objects);
            }
        });
        super.onPostExecute(objects);
    }

    private ArrayList<Tweet> jsonToTweet(JSONObject response) {
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray statuses = response.getJSONArray(TAG_STATUSES);

            for (int i = 0; i < statuses.length(); i++) {
                JSONObject jsonObject = statuses.getJSONObject(i);
                JSONObject user = jsonObject.getJSONObject(TAG_USER);
                String screenName = user.getString(TAG_SCREEN_NAME);
                String location = user.getString(TAG_LOCATION);
                String text = jsonObject.getString(TAG_TEXT);

                Tweet tweet = new Tweet();
                tweet.setScreenName(screenName);
                tweet.setLocation(location);
                tweet.setText(text);

                tweets.add(tweet);
            }
            return tweets;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }
}
