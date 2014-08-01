package com.github.mecharyry.tweetlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.BuildConfig;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

import java.util.List;

public class MyStreamFragment extends Fragment {

    public static final String ACTION_VIEW_MY_STREAM_TWEETS = BuildConfig.PACKAGE_NAME + ".ACTION_VIEW_MY_STREAM_TWEETS";

    private final TaskExecutor taskExecutor;
    private TweetAdapter tweetAdapter;
    private TaskFactory taskFactory;
    private ListView listView;
    private View view;

    public MyStreamFragment() {
        this.taskExecutor = new TaskExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_stream, container, false);

        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);

        tweetAdapter = TweetAdapter.newInstance(getActivity());
        listView = (ListView) view.findViewById(R.id.listview_mystream);
        listView.setAdapter(tweetAdapter);
        taskExecutor.execute(updateListCallback, taskFactory.requestMyStreamTweets());

        return view;
    }

    private final TaskCompleted<List<Tweet>> updateListCallback = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetAdapter.updateTweets(response);
        }
    };
}
