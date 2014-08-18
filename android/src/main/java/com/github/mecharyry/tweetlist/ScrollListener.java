package com.github.mecharyry.tweetlist;


import android.widget.AbsListView;

public class ScrollListener implements AbsListView.OnScrollListener {

    private int totalLoadedCount = 0;
    private Callback callback;

    public interface Callback {
        void onScroll();
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
            if (callback != null) {
                callback.onScroll();
            }
        }
    }
}
