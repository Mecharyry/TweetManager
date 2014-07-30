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

public class MyStreamActivity extends Activity {

    public static final String MY_STREAM_TWEETS_INTENT = "com.github.mecharyry.MY_STREAM_INTENT";
    private TweetAdapter tweetAdapter;
    private RequestFactory requestFactory;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stream);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        requestFactory = RequestFactory.newInstance(accessToken);

        tweetAdapter = TweetAdapter.newInstance(this);
        listView = (ListView) findViewById(R.id.listview_mystream);
        listView.setAdapter(tweetAdapter);
        requestFactory.requestMyStreamTweets(updateListCallback);

    }

    private final PerformGetTask.Callback updateListCallback = new PerformGetTask.Callback<List<Tweet>>() {

        @Override
        public void onGetResponse(List<Tweet> tweets) {
            tweetAdapter.updateTweets(tweets);
        }
    };
}
