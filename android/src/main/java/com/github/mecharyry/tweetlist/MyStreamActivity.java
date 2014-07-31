package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.BuildConfig;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskFactory;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;

import java.util.List;

public class MyStreamActivity extends Activity {

    public static final String ACTION_VIEW_MY_STREAM_TWEETS = BuildConfig.PACKAGE_NAME + ".ACTION_VIEW_MY_STREAM_TWEETS";

    private final TaskExecutor taskExecutor;
    private TweetAdapter tweetAdapter;
    private TaskFactory taskFactory;
    private ListView listView;

    public MyStreamActivity() {
        this.taskExecutor = new TaskExecutor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stream);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);

        tweetAdapter = TweetAdapter.newInstance(this);
        listView = (ListView) findViewById(R.id.listview_mystream);
        listView.setAdapter(tweetAdapter);
        taskExecutor.execute(updateListCallback, taskFactory.requestMyStreamTweets());
    }

    private final TaskCompleted<List<Tweet>> updateListCallback = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetAdapter.updateTweets(response);
        }
    };
}
