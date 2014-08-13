package com.github.mecharyry.db.task;

import android.content.Context;
import android.os.AsyncTask;

import com.github.mecharyry.db.Database;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.lang.ref.WeakReference;
import java.util.List;

public class RetrieveDevTweetsTask extends AsyncTask<Void, Void, List<Tweet>> {

    private final Database database;
    private WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrievedDevTweets(List<Tweet> tweets);
    }

    public static RetrieveDevTweetsTask newInstance(Callback callback, Context context) {
        Database database = Database.newInstance(context);
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        return new RetrieveDevTweetsTask(database, callbackWeakReference);
    }

    public RetrieveDevTweetsTask(Database database, WeakReference<Callback> callbackWeakReference) {
        this.database = database;
        this.callbackWeakReference = callbackWeakReference;
    }

    @Override
    protected List<Tweet> doInBackground(Void... params) {
        database.open();
        List<Tweet> tweets = database.getTweetsByCategory(Tweet.Category.ANDROID_DEV_TWEETS);
        database.close();
        return tweets;
    }

    @Override
    protected void onPostExecute(List<Tweet> tweets) {
        super.onPostExecute(tweets);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrievedDevTweets(tweets);
        }
    }
}
