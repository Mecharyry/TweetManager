package com.github.mecharyry.db.task;

import android.content.Context;
import android.os.AsyncTask;

import com.github.mecharyry.db.Database;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.util.List;

public class InsertIntoDatabaseTask extends AsyncTask<List<Tweet>, Void, Boolean> {

    public static final int FIRST_INDEX = 0;
    private final Database database;

    public static InsertIntoDatabaseTask newInstance(Context context) {
        Database database = Database.getInstance(context);
        return new InsertIntoDatabaseTask(database);
    }

    public InsertIntoDatabaseTask(Database database) {
        this.database = database;
    }

    @Override
    protected Boolean doInBackground(List<Tweet>... params) {
        database.insertTweets(params[FIRST_INDEX]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
