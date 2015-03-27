package com.github.mecharyry.tweetlist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.mecharyry.tweetlist.StreamType;

public class TwitterSlidePagerAdapter extends FragmentStatePagerAdapter {

    TwitterSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return getStreamType(position).getFragment();
    }

    private StreamType getStreamType(int position) {
        return StreamType.values()[position];
    }

    @Override
    public int getCount() {
        return StreamType.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getStreamTypeDisplayName(position);
    }

    private String getStreamTypeDisplayName(int position) {
        return StreamType.values()[position].getDisplayName();
    }
}
