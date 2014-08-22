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

public class HashtagToContentValuesParser implements Parser<JSONObject, ContentValues[]> {

    private static final String KEY_ID = "id_str";
    private static final String KEY_STATUSES = "statuses";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_THUMB_IMAGE = "profile_image_url";
    public static final String ERROR_JSON_OBJECT_MESSAGE = "While parsing json object to list of tweets.";
    private final ImageRetriever imageRetriever;
    private final BitmapToByteArrayParser parser;

    public static HashtagToContentValuesParser newInstance(ImageRetriever imageRetriever) {
        BitmapToByteArrayParser parser = new BitmapToByteArrayParser();
        return new HashtagToContentValuesParser(imageRetriever, parser);
    }

    HashtagToContentValuesParser(ImageRetriever imageRetriever, BitmapToByteArrayParser parser) {
        this.imageRetriever = imageRetriever;
        this.parser = parser;
    }


    @Override
    public ContentValues[] parse(JSONObject jsonObject) {
        try {
            JSONArray statuses = jsonObject.getJSONArray(KEY_STATUSES);
            int numberOfStatuses = statuses.length();
            ContentValues[] contentValues = new ContentValues[numberOfStatuses];

            for (int statusIndex = 0; statusIndex < numberOfStatuses; statusIndex++) {
                JSONObject innerJsonObject = statuses.getJSONObject(statusIndex);
                ContentValues contentValue = extractContentValues(innerJsonObject);
                contentValues[statusIndex] = contentValue;
            }
            return contentValues;
        } catch (JSONException e) {
            throw DeveloperError.because(ERROR_JSON_OBJECT_MESSAGE, e);
        }
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
            bitmap = Bitmap.createBitmap(0,0, Bitmap.Config.ALPHA_8);
        }

        byte[] imageData = parser.parse(bitmap);

        ContentValues values = new ContentValues();
        values.put(TweetTable.COLUMNS.COLUMN_ID.getColumnHeader(), id);
        values.put(TweetTable.COLUMNS.COLUMN_SCREEN_NAME.getColumnHeader(), screenName);
        values.put(TweetTable.COLUMNS.COLUMN_LOCATION.getColumnHeader(), location);
        values.put(TweetTable.COLUMNS.COLUMN_TWEET_TEXT.getColumnHeader(), text);
        values.put(TweetTable.COLUMNS.COLUMN_THUMB_IMAGE.getColumnHeader(), imageData);
        values.put(TweetTable.COLUMNS.COLUMN_CATEGORY.getColumnHeader(), TweetTable.Category.ANDROID_DEV_TWEETS.toString());

        return values;
    }
}
