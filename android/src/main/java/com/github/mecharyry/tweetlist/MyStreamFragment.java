package com.github.mecharyry.tweetlist;

import android.app.Activity;
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
import com.github.mecharyry.db.task.RetrieveMyStreamTask;
import com.github.mecharyry.tweetlist.adapter.TweetAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

import java.util.List;

public class MyStreamFragment extends Fragment {

    private static final String TAG = MyStreamFragment.class.getSimpleName();
    private TweetAdapter tweetAdapter;
    private TaskExecutor taskExecutor;
    private TaskFactory taskFactory;

    public MyStreamFragment() {
        taskExecutor = new TaskExecutor();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
        tweetAdapter = TweetAdapter.newInstance(activity);
        RetrieveMyStreamTask.newInstance(onMyStreamTweetsFromDb, activity).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_stream, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listview_mystream);
        listView.setAdapter(tweetAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskExecutor.execute(onMyStreamTweetsReceived, taskFactory.requestMyStreamTweets());
    }

    private final TaskCompleted<List<Tweet>> onMyStreamTweetsReceived = new TaskCompleted<List<Tweet>>() {
        @Override
        public void taskCompleted(List<Tweet> response) {
            tweetAdapter.updateTweets(response);
            InsertIntoDatabaseTask.newInstance(getActivity()).execute(response);
        }
    };

    private final RetrieveMyStreamTask.Callback onMyStreamTweetsFromDb = new RetrieveMyStreamTask.Callback() {
        @Override
        public void onRetrievedMyStream(List<Tweet> tweets) {
            tweetAdapter.updateTweets(tweets);
        }
    };
}
