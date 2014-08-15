package com.github.mecharyry.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TweetContentProvider extends ContentProvider {

    public static final String TAG = TweetContentProvider.class.getSimpleName();
    private static final String AUTHORITY = "com.github.mecharyry.db.databaseProvider";
    private static final String BASE_PATH = "tweet";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private ExtendedSQLiteOpenHelper database;

    @Override
    public boolean onCreate() {
        database = new ExtendedSQLiteOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // TODO: Remove Query Builder.
        queryBuilder.setTables(TweetTable.TABLES.TWEET_TABLE.getTableName());

        SQLiteDatabase db = database.getWritableDatabase();
        TweetTable.COLUMNS.values();
        Cursor cursor = db.query(false, TweetTable.TABLES.TWEET_TABLE.getTableName(), TweetTable.COLUMNS.names(), selection, selectionArgs, null, null, null, null);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsInserted = 0;

        for (ContentValues value : values) {
            sqlDB.insertWithOnConflict(TweetTable.TABLES.TWEET_TABLE.getTableName(), null, value, SQLiteDatabase.CONFLICT_REPLACE);
            rowsInserted++;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        sqlDB.close();
        return rowsInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
