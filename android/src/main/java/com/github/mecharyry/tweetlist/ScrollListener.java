package com.github.mecharyry.tweetlist;

import android.widget.AbsListView;

public class ScrollListener implements AbsListView.OnScrollListener {

    public static final String TAG = ScrollListener.class.getSimpleName();
    private int totalLoadedCount = 0;
    private int previousFirstVisibleItem = 0;
    private Callback callback;

    public interface Callback {
        void onLoadMore();

        void onHideTabs();

        void onShowTabs();
    }

    public ScrollListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int halfWay = totalItemCount / 2;
        int distanceFromEnd = firstVisibleItem + halfWay;

        boolean overHalfWay = distanceFromEnd >= totalItemCount;
        boolean atEnd = totalItemCount != totalLoadedCount;

        boolean loadMore = atEnd && overHalfWay;

        if (loadMore) {
            totalLoadedCount = totalItemCount;
            callback.onLoadMore();
        }

        if (firstVisibleItem > previousFirstVisibleItem + 1) {
            callback.onHideTabs();
            previousFirstVisibleItem = firstVisibleItem;
        } else if (firstVisibleItem < previousFirstVisibleItem - 1) {
            callback.onShowTabs();
            previousFirstVisibleItem = firstVisibleItem;
        }
    }
}
