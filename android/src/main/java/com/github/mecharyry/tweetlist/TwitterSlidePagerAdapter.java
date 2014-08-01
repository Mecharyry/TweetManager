package com.github.mecharyry.tweetlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class TwitterSlidePagerAdapter extends FragmentStatePagerAdapter {

    private final String tabtitles[] = new String[]{"Home Stream", "Android Dev"};

    public static TwitterSlidePagerAdapter newInstance(FragmentManager fragmentManager) {
        return new TwitterSlidePagerAdapter(fragmentManager);
    }

    TwitterSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyStreamFragment();
            case 1:
                return new AndroidDevTweetsFragment();
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
