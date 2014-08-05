package com.github.mecharyry.tweetlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private final TaskExecutor taskExecutor;
    private TweetAdapter tweetAdapter;
    private View view;

    public MyStreamFragment() {
        this.taskExecutor = new TaskExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_stream, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        TaskFactory taskFactory = TaskFactory.newInstance(accessToken);

        tweetAdapter = TweetAdapter.newInstance(getActivity());
        ListView listView = (ListView) view.findViewById(R.id.listview_mystream);
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
