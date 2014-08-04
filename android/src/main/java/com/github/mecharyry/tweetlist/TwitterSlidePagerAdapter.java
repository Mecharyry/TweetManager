package com.github.mecharyry.tweetlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.mecharyry.DeveloperError;

class TwitterSlidePagerAdapter extends FragmentStatePagerAdapter {

    public static TwitterSlidePagerAdapter newInstance(FragmentManager fragmentManager) {
        return new TwitterSlidePagerAdapter(fragmentManager);
    }

    TwitterSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        StreamType streamType = getStreamType(position);
        switch (streamType) {
            case ANDROID_DEV_STREAM:
                return streamType.getFragment();
            case MY_STREAM:
                return streamType.getFragment();
            default:
                throw new DeveloperError();
        }
    }

    private StreamType getStreamType(int position) {
        return StreamType.valueOf(StreamType.values()[position].name());
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
