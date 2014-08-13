package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.db.Database;
import com.github.mecharyry.db.task.InsertIntoDatabaseTask;
import com.github.mecharyry.db.task.RetrieveTweetsFromDBTask;
import com.github.mecharyry.tweetlist.adapter.TweetCursorAdapter;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

import java.util.List;

public class MyStreamFragment extends Fragment {

    private static final String TAG = MyStreamFragment.class.getSimpleName();
    private TweetCursorAdapter tweetAdapter;
    private TaskExecutor taskExecutor;
    private TaskFactory taskFactory;
    private Database database;
    private ListView listView;

    public MyStreamFragment() {
        taskExecutor = new TaskExecutor();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
        database = Database.getInstance(activity);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Cursor tweetsByCategoryCursor = database.getTweetsByCategory(Tweet.Category.MY_STREAM_TWEETS);
                tweetAdapter = TweetCursorAdapter.newInstance(activity, tweetsByCategoryCursor, false);
                listView.setAdapter(tweetAdapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_stream, container, false);
        listView = (ListView) view.findViewById(R.id.listview_mystream);
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
            InsertIntoDatabaseTask.newInstance(getActivity()).execute(response);
            RetrieveTweetsFromDBTask.newInstance(onMyStreamTweetsFromDb, getActivity()).executeTask(Tweet.Category.MY_STREAM_TWEETS);
        }
    };

    private final RetrieveTweetsFromDBTask.Callback onMyStreamTweetsFromDb = new RetrieveTweetsFromDBTask.Callback() {
        @Override
        public void onRetrievedTweetsFromDB(Cursor tweets) {
            tweetAdapter.changeCursor(tweets);
        }
    };
}
