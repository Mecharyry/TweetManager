package com.github.mecharyry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.parser.ParserFactory;

import java.util.List;

public final class Database {

    private static final String TAG = Database.class.getSimpleName();
    private final ExtendedSQLiteOpenHelper helper;
    private final ParserFactory parserFactory;
    private final SQLiteDatabase sqLiteDatabase;

    public static Database newInstance(Context context) {
        ExtendedSQLiteOpenHelper helper = new ExtendedSQLiteOpenHelper(context);
        ParserFactory parserFactory = ParserFactory.newInstance();
        return new Database(helper, parserFactory);
    }

    Database(ExtendedSQLiteOpenHelper helper, ParserFactory parserFactory) {
        this.helper = helper;
        this.parserFactory = parserFactory;

        sqLiteDatabase = helper.getWritableDatabase();
    }

    private void open() {
        helper.getWritableDatabase();
    }

    private void close() {
        helper.close();
    }

    private void insertTweet(Tweet tweet) {
        ContentValues values = new ContentValues();
        values.put(TweetTable.COLUMN_ID, tweet.getId());
        values.put(TweetTable.COLUMN_SCREEN_NAME, tweet.getScreenName());
        values.put(TweetTable.COLUMN_LOCATION, tweet.getLocation());
        values.put(TweetTable.COLUMN_TWEET_TEXT, tweet.getText());
        values.put(TweetTable.COLUMN_CATEGORY, tweet.getCategory().toString());

        byte[] imageData = parserFactory.bitmapToByteArrayParser().parse(tweet.getThumbImage());
        values.put(TweetTable.COLUMN_THUMB_IMAGE, imageData);

        sqLiteDatabase.insertWithOnConflict(TweetTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void insertTweets(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            insertTweet(tweet);
        }
    }

    public Cursor getTweetsByCategory(Tweet.Category category) {
        String selection = TweetTable.COLUMN_CATEGORY + " LIKE '" + category + "'";
        Cursor query = sqLiteDatabase.query(TweetTable.TABLE_NAME, TweetTable.ALL_COLUMNS, selection, null, null, null, null);
        return query;
    }
}
