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

public class AndroidDevTweetsActivity extends Activity {

    public static final String ACTION_VIEW_ANDROID_DEV_TWEETS = BuildConfig.PACKAGE_NAME + ".ACTION_VIEW_ANDROID_DEV_TWEETS";
    private TweetAdapter tweetArrayAdapter;
    private final TaskExecutor taskExecutor;
    private TaskFactory taskFactory;
    private ListView listView;

    public AndroidDevTweetsActivity() {
        this.taskExecutor = new TaskExecutor();
    }

    private final TaskCompleted<List<Tweet>> updateListCallback = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetArrayAdapter.updateTweets(response);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_dev_tweets);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
        tweetArrayAdapter = TweetAdapter.newInstance(this);
        listView = (ListView) findViewById(R.id.listview_androiddev_tweets);
        listView.setAdapter(tweetArrayAdapter);

        taskExecutor.execute(updateListCallback, taskFactory.requestAndroidDevTweets());
        taskFactory.requestAndroidDevTweets();
    }
}
