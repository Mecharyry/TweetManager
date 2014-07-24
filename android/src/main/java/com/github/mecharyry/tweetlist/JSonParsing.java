package com.github.mecharyry.tweetlist;

import android.util.Log;

import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParsing {
    private static final String TAG = "PerformJsonParsingTask";
    private static final String TAG_STATUSES = "statuses";
    private static final String TAG_TEXT = "text";
    private static final String TAG_USER = "user";
    private static final String TAG_SCREEN_NAME = "screen_name";
    private static final String TAG_LOCATION = "location";

    public static JsonParsing newInstance() {
        return new JsonParsing();
    }

    public List<Tweet> TweetsByHashTag(JSONObject jsonObject) {
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray statuses = jsonObject.getJSONArray(TAG_STATUSES);

            for (int i = 0; i < statuses.length(); i++) {
                JSONObject innerJsonObject = statuses.getJSONObject(i);
                JSONObject user = innerJsonObject.getJSONObject(TAG_USER);
                String screenName = user.getString(TAG_SCREEN_NAME);
                String location = user.getString(TAG_LOCATION);
                String text = innerJsonObject.getString(TAG_TEXT);

                Tweet tweet = new Tweet(screenName, location, text);

                tweets.add(tweet);
            }
            return tweets;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }
}
