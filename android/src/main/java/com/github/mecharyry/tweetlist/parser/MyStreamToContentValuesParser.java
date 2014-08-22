package com.github.mecharyry.tweetlist.parser;

import android.content.ContentValues;
import android.graphics.Bitmap;

import com.github.mecharyry.DeveloperError;
import com.github.mecharyry.db.TweetTable;
import com.github.mecharyry.tweetlist.ImageRetriever;
import com.github.mecharyry.tweetlist.request.RequestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyStreamToContentValuesParser implements Parser<JSONArray, ContentValues[]> {

    private static final String KEY_ID = "id_str";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    private static final String ERROR_JSON_ARRAY_MESSAGE = "While parsing json array to list of tweets.";
    private final ImageRetriever imageRetriever;
    private final BitmapToByteArrayParser parser;

    public static MyStreamToContentValuesParser newInstance(ImageRetriever imageRetriever) {
        BitmapToByteArrayParser parser = new BitmapToByteArrayParser();
        return new MyStreamToContentValuesParser(imageRetriever, parser);
    }

    public MyStreamToContentValuesParser(ImageRetriever imageRetriever, BitmapToByteArrayParser parser) {
        this.imageRetriever = imageRetriever;
        this.parser = parser;
    }

    @Override
    public ContentValues[] parse(JSONArray from) {
        ContentValues[] tweets = new ContentValues[from.length()];
        try {
            for (int tweetIndex = 0; tweetIndex < from.length(); tweetIndex++) {
                JSONObject jsonObject = from.getJSONObject(tweetIndex);
                ContentValues tweet = extractContentValues(jsonObject);
                tweets[tweetIndex] = tweet;
            }
        } catch (JSONException e) {
            throw DeveloperError.because(ERROR_JSON_ARRAY_MESSAGE, e);
        }
        return tweets;
    }

    private ContentValues extractContentValues(JSONObject jsonObject) throws JSONException {
        long id = Long.parseLong(jsonObject.getString(KEY_ID));
        JSONObject user = jsonObject.getJSONObject(KEY_USER);
        String screenName = user.getString(KEY_SCREEN_NAME);
        String location = user.getString(KEY_LOCATION);
        String text = jsonObject.getString(KEY_TEXT);
        String imgUrl = user.getString(KEY_THUMB_IMAGE);

        Bitmap bitmap;
        try {
            bitmap = imageRetriever.request(imgUrl);
        } catch (RequestException e) {
            bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8);
        }

        byte[] imageData = parser.parse(bitmap);

        ContentValues values = new ContentValues();
        values.put(TweetTable.COLUMNS.COLUMN_ID.getColumnHeader(), id);
        values.put(TweetTable.COLUMNS.COLUMN_SCREEN_NAME.getColumnHeader(), screenName);
        values.put(TweetTable.COLUMNS.COLUMN_LOCATION.getColumnHeader(), location);
        values.put(TweetTable.COLUMNS.COLUMN_TWEET_TEXT.getColumnHeader(), text);
        values.put(TweetTable.COLUMNS.COLUMN_THUMB_IMAGE.getColumnHeader(), imageData);
        values.put(TweetTable.COLUMNS.COLUMN_CATEGORY.getColumnHeader(), TweetTable.Category.MY_STREAM_TWEETS.toString());

        return values;
    }
}
