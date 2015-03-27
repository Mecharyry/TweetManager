package com.github.mecharyry.tweetlist;


import android.widget.AbsListView;

public class ScrollListener implements AbsListView.OnScrollListener {

    private int totalLoadedCount = 0;
    private Callback callback;

    public interface Callback {
        void onLoadMore();
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

        boolean isOverHalfway = distanceFromEnd >= totalItemCount;
        boolean isAtEnd = totalItemCount != totalLoadedCount;

        boolean loadMore = isAtEnd && isOverHalfway;
        if (loadMore) {
            totalLoadedCount = totalItemCount;
            callback.onLoadMore();
        }
    }
}
