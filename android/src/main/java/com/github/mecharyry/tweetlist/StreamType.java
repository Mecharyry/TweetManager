package com.github.mecharyry.tweetlist;

import android.support.v4.app.Fragment;

public enum StreamType {
    ANDROID_DEV_STREAM("Android Dev") {
        @Override
        public Fragment getFragment() {
            return new AndroidDevTweetsFragment();
        }
    }, MY_STREAM("Home Stream") {
        @Override
        public Fragment getFragment() {
            return new MyStreamFragment();
        }
    };

    private final String displayName;

    StreamType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract Fragment getFragment();

    @Override
    public String toString() {
        return super.toString();
    }

}
