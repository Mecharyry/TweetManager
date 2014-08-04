package com.github.mecharyry.tweetlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mecharyry.R;

public class TweetPagerFragment extends Fragment {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tweet_screen_slider, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new TwitterSlidePagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        view.setOnKeyListener(onBackPressed);
    }

    private final View.OnKeyListener onBackPressed = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    viewPager.getCurrentItem() == 0) {
                getActivity().onBackPressed();
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
            return true;
        }
    };
}
