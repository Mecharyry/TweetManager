package com.github.mecharyry.tweetlist;

import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import org.json.JSONArray;

import java.util.List;

public class TweetsMyStreamParser implements Parser<List<Tweet>, JSONArray> {

    private static final String TAG = "PerformJsonParsingTask";
    private static final String KEY_STATUSES = "statuses";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    private ImageRetriever imageRetriever;

    public TweetsMyStreamParser(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    @Override
    public List<Tweet> parse(JSONArray jsonObject) {
        return null;
    }
}
