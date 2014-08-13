package com.github.mecharyry.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExtendedSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    private static final String TAG = ExtendedSQLiteOpenHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "tweet.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TweetTable.TABLE_NAME +
                    " (" + TweetTable.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    TweetTable.COLUMN_SCREEN_NAME + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMN_LOCATION + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMN_THUMB_IMAGE + BLOB_TYPE + COMMA_SEP +
                    TweetTable.COLUMN_TWEET_TEXT + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMN_CATEGORY + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TweetTable.TABLE_NAME;

    public ExtendedSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.i(TAG, "Create Statement: " + SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.i(TAG, "Delete Statement: " + SQL_DELETE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

