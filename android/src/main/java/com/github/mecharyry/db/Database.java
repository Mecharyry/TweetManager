package com.github.mecharyry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private SQLiteDatabase database;
    private ExtendedSQLiteOpenHelper helper;

    public static Database newInstance(Context context) {
        ExtendedSQLiteOpenHelper helper = new ExtendedSQLiteOpenHelper(context);
        return new Database(helper);
    }

    Database(ExtendedSQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void open() {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public long insertTweet(Tweet tweet) {
        ContentValues values = new ContentValues();
        values.put(TweetTable.COLUMN_SCREEN_NAME, tweet.getScreenName());
        values.put(TweetTable.COLUMN_LOCATION, tweet.getLocation());
        values.put(TweetTable.COLUMN_TWEET_TEXT, tweet.getText());

        byte[] imageData = getBitmapAsByteArray(tweet.getThumbImage());
        values.put(TweetTable.COLUMN_THUMB_IMAGE, imageData);

        return database.insert(TweetTable.TABLE_NAME, null, values);
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public List<Tweet> getAllTweets() {
        List<Tweet> tweets = new ArrayList<Tweet>();
        Cursor cursor = database.query(TweetTable.TABLE_NAME, TweetTable.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tweet tweet = cursorToTweet(cursor);
            tweets.add(tweet);

            cursor.moveToNext();
        }
        return tweets;
    }

    private Tweet cursorToTweet(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_ID);
        int locationColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_LOCATION);
        int screenNameColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_SCREEN_NAME);
        int textColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_TWEET_TEXT);
        int bitmapColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_THUMB_IMAGE);

        int id = cursor.getInt(idColumnIndex);
        String location = cursor.getString(locationColumnIndex);
        String screenName = cursor.getString(screenNameColumnIndex);
        String text = cursor.getString(textColumnIndex);
        Bitmap thumbImage = getByteArrayAsBitmap(cursor, bitmapColumnIndex);

        Tweet tweet = new Tweet(screenName, location, text, thumbImage);
        tweet.setId(id);
        return tweet;
    }

    private Bitmap getByteArrayAsBitmap(Cursor cursor, int bitmapColumnIndex) {
        byte[] imageData = cursor.getBlob(bitmapColumnIndex);
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }
}
