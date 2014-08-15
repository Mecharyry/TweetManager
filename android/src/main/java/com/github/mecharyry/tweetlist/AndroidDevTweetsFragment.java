package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.db.TweetContentProvider;
import com.github.mecharyry.db.TweetTable;
import com.github.mecharyry.tweetlist.adapter.TweetCursorAdapter;
import com.github.mecharyry.tweetlist.task.TaskCompleted;
import com.github.mecharyry.tweetlist.task.TaskExecutor;
import com.github.mecharyry.tweetlist.task.TaskFactory;

public class AndroidDevTweetsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {

    private static final int LOADER_MANAGER_ID = 0xa9;

    private static final String TAG = AndroidDevTweetsFragment.class.getSimpleName();
    private static final String QUERY_SELECTION = TweetTable.COLUMNS.COLUMN_CATEGORY.getColumnHeader() + "= ?";
    private static final String[] QUERY_SELECTION_ARGS = {TweetTable.Category.ANDROID_DEV_TWEETS.toString()};
    private static final String QUERY_ORDER_BY = TweetTable.COLUMNS.COLUMN_ID.getColumnHeader() + " DESC";

    private int totalLoadedCount = 0;

    private TweetCursorAdapter tweetAdapter;
    private TaskExecutor taskExecutor;
    private TaskFactory taskFactory;
    private ListView listView;

    public AndroidDevTweetsFragment() {
        taskExecutor = new TaskExecutor();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AccessTokenPreferences accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        AccessToken accessToken = accessTokenPreferences.retrieveAccessToken();
        taskFactory = TaskFactory.newInstance(accessToken);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android_dev_tweets, container, false);
        listView = (ListView) view.findViewById(R.id.listview_androiddev_tweets);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskExecutor.execute(onAndroidDevTweetsReceived, taskFactory.requestAndroidDevTweets());

        tweetAdapter = TweetCursorAdapter.newInstance(getLayoutInflater(savedInstanceState), null, false);
        listView.setAdapter(tweetAdapter);
        listView.setOnScrollListener(this);

        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
    }

    private final TaskCompleted<ContentValues[]> onAndroidDevTweetsReceived = new TaskCompleted<ContentValues[]>() {
        @Override
        public void taskCompleted(ContentValues[] response) {
            getActivity().getContentResolver().bulkInsert(TweetContentProvider.CONTENT_URI, response);
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), TweetContentProvider.CONTENT_URI,
                TweetTable.COLUMNS.names(), QUERY_SELECTION, QUERY_SELECTION_ARGS, QUERY_ORDER_BY);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        tweetAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {
        tweetAdapter.swapCursor(null);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int indexFirstVisible, int numberVisible, int totalCount) {
        int halfWay = totalCount / 2;
        int distanceFromEnd = indexFirstVisible + halfWay;

        boolean overHalfWay = distanceFromEnd >= totalCount;
        boolean atEnd = totalCount != totalLoadedCount;

        boolean loadMore = atEnd && overHalfWay;
        if (loadMore) {
            totalLoadedCount = totalCount;
            int finalListViewItemIndex = tweetAdapter.getCount() - 1;

            Cursor cursor = (Cursor) tweetAdapter.getItem(finalListViewItemIndex);
            long id = cursor.getLong(cursor.getColumnIndex(TweetTable.COLUMNS.COLUMN_ID.getColumnHeader()));
            taskExecutor.execute(onAndroidDevTweetsReceived, taskFactory.requestAndroidDevTweetsBeforeId(id));
        }
    }
}
