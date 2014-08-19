package com.github.mecharyry.tweetlist;

import android.support.v4.view.ViewPager;

public class OnPageChangeListener implements ViewPager.OnPageChangeListener {

    private Callback callback;

    public interface Callback {
        void onPageSelected();
    }

    public OnPageChangeListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        callback.onPageSelected();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
