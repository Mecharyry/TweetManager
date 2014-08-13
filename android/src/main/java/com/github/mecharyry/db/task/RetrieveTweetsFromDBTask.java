package com.github.mecharyry.db.task;

import android.content.Context;
import android.os.AsyncTask;

import com.github.mecharyry.db.TweetDatabaseAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.lang.ref.WeakReference;
import java.util.List;

public class RetrieveTweetsFromDBTask extends AsyncTask<Tweet.Category, Void, List<Tweet>> {

    public static final int FIRST_INDEX = 0;
    private final TweetDatabaseAdapter tweetDatabaseAdapter;
    private WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrievedTweetsFromDB(List<Tweet> tweets);
    }

    public static RetrieveTweetsFromDBTask newInstance(Callback callback, Context context) {
        TweetDatabaseAdapter tweetDatabaseAdapter = TweetDatabaseAdapter.newInstance(context);
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        return new RetrieveTweetsFromDBTask(tweetDatabaseAdapter, callbackWeakReference);
    }

    public RetrieveTweetsFromDBTask(TweetDatabaseAdapter tweetDatabaseAdapter, WeakReference<Callback> callbackWeakReference) {
        this.tweetDatabaseAdapter = tweetDatabaseAdapter;
        this.callbackWeakReference = callbackWeakReference;
    }

    public void executeTask(Tweet.Category category) {
        this.execute(category);
    }

    @Override
    protected List<Tweet> doInBackground(Tweet.Category... params) {
        tweetDatabaseAdapter.open();
        List<Tweet> tweets = tweetDatabaseAdapter.getTweetsByCategory(params[FIRST_INDEX]);
        tweetDatabaseAdapter.close();
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
