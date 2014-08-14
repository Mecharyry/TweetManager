package com.github.mecharyry.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class TweetContentProvider extends ContentProvider {

    public static final String TAG = TweetContentProvider.class.getSimpleName();
    private ExtendedSQLiteOpenHelper database;

    private static final int TWEETS = 10;
    private static final int TWEET_ID = 20;

    private static final String AUTHORITY = "com.github.mecharyry.db.databaseProvider";

    private static final String BASE_PATH = "tweet";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/tweets";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/tweet";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TWEETS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TWEET_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ExtendedSQLiteOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TweetTable.TABLE_NAME);

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.query(false, TweetTable.TABLE_NAME, TweetTable.ALL_COLUMNS, selection, selectionArgs, null, null, null, null);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsInserted = 0;

        for(ContentValues value : values){
            sqlDB.insertWithOnConflict(TweetTable.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
            rowsInserted++;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        sqlDB.close();
        return rowsInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
