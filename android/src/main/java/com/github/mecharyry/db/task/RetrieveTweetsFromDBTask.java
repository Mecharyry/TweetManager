package com.github.mecharyry.db.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.github.mecharyry.db.Database;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.lang.ref.WeakReference;

public class RetrieveTweetsFromDBTask extends AsyncTask<Tweet.Category, Void, Cursor> {

    public static final int FIRST_INDEX = 0;
    private final Database database;
    private WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrievedTweetsFromDB(Cursor tweets);
    }

    public static RetrieveTweetsFromDBTask newInstance(Callback callback, Context context) {
        Database database = Database.getInstance(context);
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        return new RetrieveTweetsFromDBTask(database, callbackWeakReference);
    }

    public RetrieveTweetsFromDBTask(Database database, WeakReference<Callback> callbackWeakReference) {
        this.database = database;
        this.callbackWeakReference = callbackWeakReference;
    }

    public void executeTask(Tweet.Category category) {
        this.execute(category);
    }

    @Override
    protected Cursor doInBackground(Tweet.Category... params) {
        Cursor cursor = database.getTweetsByCategory(params[FIRST_INDEX]);
        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor tweets) {
        super.onPostExecute(tweets);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrievedTweetsFromDB(tweets);
        }
    }
}
