package com.github.mecharyry.tweetlist;

import android.graphics.Bitmap;
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
    private static final String TAG_THUMB_IMAGE = "profile_image_url";

    private final ImageDownloader imageDownloader;

    public JsonParsing() {
        imageDownloader = new ImageDownloader();
    }

    public List<Tweet> TweetsByHashTag(JSONObject jsonObject) {
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray statuses = jsonObject.getJSONArray(TAG_STATUSES);

            for (int statusIndex = 0; statusIndex < statuses.length(); statusIndex++) {
                JSONObject innerJsonObject = statuses.getJSONObject(statusIndex);
                JSONObject user = innerJsonObject.getJSONObject(TAG_USER);
                String screenName = user.getString(TAG_SCREEN_NAME);
                String location = user.getString(TAG_LOCATION);
                String text = innerJsonObject.getString(TAG_TEXT);
                String imgUrl = user.getString(TAG_THUMB_IMAGE);

                Bitmap bitmap = imageDownloader.retrieveBitmap(imgUrl);

                Tweet tweet = new Tweet(screenName, location, text, bitmap);

                tweets.add(tweet);
            }
            return tweets;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }


}
