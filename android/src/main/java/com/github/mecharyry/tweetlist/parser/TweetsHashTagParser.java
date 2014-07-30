package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.mecharyry.tweetlist.ImageRetriever;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TweetsHashtagParser implements Parser<List<Tweet>, JSONObject> {

    private static final String TAG = "PerformJsonParsingTask";
    private static final String KEY_STATUSES = "statuses";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    private final ImageRetriever imageRetriever;

    public TweetsHashtagParser(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    @Override
    public List<Tweet> parse(JSONObject jsonObject) {
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray statuses = jsonObject.getJSONArray(KEY_STATUSES);

            for (int statusIndex = 0; statusIndex < statuses.length(); statusIndex++) {
                JSONObject innerJsonObject = statuses.getJSONObject(statusIndex);
                JSONObject user = innerJsonObject.getJSONObject(KEY_USER);
                String screenName = user.getString(KEY_SCREEN_NAME);
                String location = user.getString(KEY_LOCATION);
                String text = innerJsonObject.getString(KEY_TEXT);
                String imgUrl = user.getString(KEY_THUMB_IMAGE);

                Bitmap bitmap = imageRetriever.retrieveBitmap(imgUrl);

                Tweet tweet = new Tweet(screenName, location, text, bitmap);

                tweets.add(tweet);
            }
            return tweets;
        } catch (JSONException e) {
            Log.e(TAG, "While parsing json object to list of tweets.", e);
        }
        return new ArrayList<Tweet>();
    }
}
