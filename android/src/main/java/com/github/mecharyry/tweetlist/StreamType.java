package com.github.mecharyry.tweetlist;

import android.support.v4.app.Fragment;

public enum StreamType {
    ANDROID_DEV_STREAM("Android Dev", new AndroidDevTweetsFragment()), MY_STREAM("Home Stream", new MyStreamFragment());

    private final String displayName;
    private final Fragment fragment;

    StreamType(String displayName, Fragment fragment) {
        this.displayName = displayName;
        this.fragment = fragment;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
