package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.db.task.InsertIntoDatabaseTask;
import com.github.mecharyry.db.task.RetrieveTweetsFromDBTask;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

import java.util.List;

public class AndroidDevTweetsFragment extends Fragment {

    public static final String TAG = AndroidDevTweetsFragment.class.getSimpleName();
    private TweetAdapter tweetAdapter;
    private TaskExecutor taskExecutor;
    private TaskFactory taskFactory;

    public AndroidDevTweetsFragment() {
        taskExecutor = new TaskExecutor();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
        tweetAdapter = TweetAdapter.newInstance(activity);
        RetrieveTweetsFromDBTask.newInstance(onRetrievedDevTweetsFromDb, activity).executeTask(Tweet.Category.ANDROID_DEV_TWEETS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android_dev_tweets, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listview_androiddev_tweets);
        listView.setAdapter(tweetAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskExecutor.execute(onAndroidDevTweetsReceived, taskFactory.requestAndroidDevTweets());
    }

    private final TaskCompleted<List<Tweet>> onAndroidDevTweetsReceived = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetAdapter.updateTweets(response);
            InsertIntoDatabaseTask.newInstance(getActivity()).execute(response);
        }
    };

    private final RetrieveTweetsFromDBTask.Callback onRetrievedDevTweetsFromDb = new RetrieveTweetsFromDBTask.Callback() {
        @Override
        public void onRetrievedTweetsFromDB(Cursor tweets) {
            //tweetAdapter.updateTweets(tweets);
        }
    };
}
