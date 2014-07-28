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

    private TweetAdapter tweetArrayAdapter;
    private RequestManager requestManager;
    private ListView listView;

    private final PerformGetTask.Callback updateListCallback = new PerformGetTask.Callback() {
        @Override
        public void onGetResponse(List<Tweet> tweets) {
            tweetArrayAdapter.updateTweets(tweets);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        requestManager = RequestManager.newInstance(accessToken);
        this.tweetArrayAdapter = TweetAdapter.newInstance(this);
        setContentView(R.layout.activity_android_dev_tweets);
        listView = (ListView) findViewById(R.id.listview_androiddev_tweets);
        listView.setAdapter(tweetArrayAdapter);
        requestManager.requestAndroidDevTweets(updateListCallback);
    }
}
