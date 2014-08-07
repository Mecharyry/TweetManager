package com.github.mecharyry.tweetlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

import java.util.List;

public class AndroidDevTweetsFragment extends Fragment {

    private TweetAdapter tweetArrayAdapter;
    private final TaskExecutor taskExecutor;
    private TaskFactory taskFactory;
    private ListView listView;
    private View view;

    public AndroidDevTweetsFragment() {
        this.taskExecutor = new TaskExecutor();
    }

    private final TaskCompleted<List<Tweet>> updateListCallback = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetArrayAdapter.updateTweets(response);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_android_dev_tweets, container, false);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
        tweetArrayAdapter = TweetAdapter.newInstance(getActivity());
        listView = (ListView) view.findViewById(R.id.listview_androiddev_tweets);
        listView.setAdapter(tweetArrayAdapter);

        taskExecutor.execute(updateListCallback, taskFactory.requestAndroidDevTweets());
        taskFactory.requestAndroidDevTweets();

        return view;
    }
}
