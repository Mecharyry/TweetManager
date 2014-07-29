package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.PerformGetTask;

import java.util.List;

public class AndroidDevTweetsActivity extends Activity {

    public static final String ANDROID_DEV_TWEETS_INTENT = "com.github.mecharyry.ANDROID_TWEETS_INTENT";
    private TweetAdapter tweetArrayAdapter;
    private RequestManager requestManager;
    private ListView listView;

    private final PerformGetTask.Callback updateListCallback = new PerformGetTask.Callback<List<Tweet>>() {
        @Override
        public void onGetResponse(List<Tweet> tweets) {
            tweetArrayAdapter.updateTweets(tweets);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_dev_tweets);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        requestManager = RequestManager.newInstance(accessToken);
        this.tweetArrayAdapter = TweetAdapter.newInstance(this);
        listView = (ListView) findViewById(R.id.listview_androiddev_tweets);
        listView.setAdapter(tweetArrayAdapter);
        requestManager.requestAndroidDevTweets(updateListCallback);
    }
}
