package com.github.mecharyry.db.task;

import android.content.Context;
import android.os.AsyncTask;

import com.github.mecharyry.db.Database;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.lang.ref.WeakReference;
import java.util.List;

public class RetrieveTweetsFromDBTask extends AsyncTask<Tweet.Category, Void, List<Tweet>> {

    public static final int FIRST_INDEX = 0;
    private final Database database;
    private WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrievedTweetsFromDB(List<Tweet> tweets);
    }

    public static RetrieveTweetsFromDBTask newInstance(Callback callback, Context context) {
        Database database = Database.newInstance(context);
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
    protected List<Tweet> doInBackground(Tweet.Category... params) {
        database.open();
        List<Tweet> tweets = database.getTweetsByCategory(params[FIRST_INDEX]);
        database.close();
        return tweets;
    }

    @Override
    protected void onPostExecute(List<Tweet> tweets) {
        super.onPostExecute(tweets);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrievedTweetsFromDB(tweets);
        }
    }
}
