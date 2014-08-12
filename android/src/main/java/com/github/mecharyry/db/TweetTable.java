package com.github.mecharyry.db;

public final class TweetTable {

    public static final String TABLE_NAME = "tweet";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCREEN_NAME = "screen_name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TWEET_TEXT = "text";
    public static final String COLUMN_THUMB_IMAGE = "thumb_image";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_SCREEN_NAME, COLUMN_LOCATION,
            COLUMN_TWEET_TEXT, COLUMN_THUMB_IMAGE};
}
