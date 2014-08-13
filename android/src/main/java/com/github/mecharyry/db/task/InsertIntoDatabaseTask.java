package com.github.mecharyry.db.task;

import android.content.Context;
import android.os.AsyncTask;

import com.github.mecharyry.db.TweetDatabaseAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.util.List;

public class InsertIntoDatabaseTask extends AsyncTask<List<Tweet>, Void, Boolean> {

    public static final int FIRST_INDEX = 0;
    private final TweetDatabaseAdapter tweetDatabaseAdapter;

    public static InsertIntoDatabaseTask newInstance(Context context) {
        TweetDatabaseAdapter tweetDatabaseAdapter = TweetDatabaseAdapter.newInstance(context);
        return new InsertIntoDatabaseTask(tweetDatabaseAdapter);
    }

    public InsertIntoDatabaseTask(TweetDatabaseAdapter tweetDatabaseAdapter) {
        this.tweetDatabaseAdapter = tweetDatabaseAdapter;
    }

    @Override
    protected Boolean doInBackground(List<Tweet>... params) {
        tweetDatabaseAdapter.open();
        boolean result = tweetDatabaseAdapter.insertTweets(params[FIRST_INDEX]);
        tweetDatabaseAdapter.close();
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
