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

public class TweetsHashtagParser implements Parser<JSONObject, List<Tweet>> {

    private static final String TAG = TweetsHashtagParser.class.getSimpleName();
    private static final String KEY_ID = "id_str";
    private static final String KEY_STATUSES = "statuses";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    private static final String VALUE_CATEGORY = "android_dev";
    public static final String ERROR_JSON_OBJECT_MESSAGE = "While parsing json object to list of tweets.";
    private final ImageRetriever imageRetriever;

    public TweetsHashtagParser(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    @Override
    public List<Tweet> parse(JSONObject jsonObject) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        try {
            JSONArray statuses = jsonObject.getJSONArray(KEY_STATUSES);

            for (int statusIndex = 0; statusIndex < statuses.length(); statusIndex++) {
                JSONObject innerJsonObject = statuses.getJSONObject(statusIndex);
                Tweet tweet = extractTweet(innerJsonObject);
                tweets.add(tweet);
            }
        } catch (JSONException e) {
            Log.e(TAG, ERROR_JSON_OBJECT_MESSAGE, e);
        }
        return tweets;
    }

    private Tweet extractTweet(JSONObject jsonObject) throws JSONException {
        long id = Long.parseLong(jsonObject.getString(KEY_ID));
        JSONObject user = jsonObject.getJSONObject(KEY_USER);
        String screenName = user.getString(KEY_SCREEN_NAME);
        String location = user.getString(KEY_LOCATION);
        String text = jsonObject.getString(KEY_TEXT);
        String imgUrl = user.getString(KEY_THUMB_IMAGE);
        Bitmap bitmap = imageRetriever.retrieveBitmap(imgUrl);

        return new Tweet(id, screenName, location, text, bitmap, VALUE_CATEGORY);
    }
}
