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

public class TweetsMyStreamParser implements Parser<JSONArray, List<Tweet>> {

    private static final String TAG = TweetsMyStreamParser.class.getSimpleName();
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    private final ImageRetriever imageRetriever;

    public TweetsMyStreamParser(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    @Override
    public List<Tweet> parse(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        try {
            for (int tweetIndex = 0; tweetIndex < jsonArray.length(); tweetIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(tweetIndex);
                Tweet tweet = extractTweet(jsonObject);
                tweets.add(tweet);
            }
        } catch (JSONException e) {
            Log.e(TAG, "While parsing json array to list of tweets.", e);
        }
        return tweets;
    }

    private Tweet extractTweet(JSONObject jsonObject) throws JSONException {
        JSONObject user = jsonObject.getJSONObject(KEY_USER);
        String screenName = user.getString(KEY_SCREEN_NAME);
        String location = user.getString(KEY_LOCATION);
        String text = jsonObject.getString(KEY_TEXT);
        String imgUrl = user.getString(KEY_THUMB_IMAGE);
        Bitmap bitmap = imageRetriever.retrieveBitmap(imgUrl);

        return new Tweet(screenName, location, text, bitmap);
    }
}
