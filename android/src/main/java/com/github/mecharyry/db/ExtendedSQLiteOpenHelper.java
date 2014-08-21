package com.github.mecharyry.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExtendedSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "tweet.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TweetTable.TABLES.TWEET_TABLE.getTableName() +
                    " (" + TweetTable.COLUMNS.COLUMN_ID.getColumnHeader() + " INTEGER PRIMARY KEY," +
                    TweetTable.COLUMNS.COLUMN_SCREEN_NAME.getColumnHeader() + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMNS.COLUMN_LOCATION.getColumnHeader() + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMNS.COLUMN_THUMB_IMAGE.getColumnHeader() + BLOB_TYPE + COMMA_SEP +
                    TweetTable.COLUMNS.COLUMN_TWEET_TEXT.getColumnHeader() + TEXT_TYPE + COMMA_SEP +
                    TweetTable.COLUMNS.COLUMN_CATEGORY.getColumnHeader() + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TweetTable.TABLES.TWEET_TABLE.getTableName();

    public ExtendedSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

