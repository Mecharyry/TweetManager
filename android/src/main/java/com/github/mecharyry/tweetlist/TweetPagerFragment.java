package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;

public class TweetPagerFragment extends Fragment {

    private ViewPager viewPager;
    private AccessTokenPreferences accessTokenPreferences;
    private Callback callback;

    public interface Callback {
        void onClearCredentials();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callback.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_screen_slider, container, false);
        accessTokenPreferences = AccessTokenPreferences.newInstance(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new TwitterSlidePagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        view.setFocusableInTouchMode(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.authentication, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear_credentials:
                accessTokenPreferences.removeAccessToken();
                callback.onClearCredentials();
                break;
            default:
                throw new RuntimeException(TweetPagerFragment.class.getSimpleName() + ": Menu item not handled.");
        }
        return true;
    }

    public boolean isViewingFirstPage(){
        return viewPager.getCurrentItem() == 0;
    }

    public boolean moveToFirstPage() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return true;
        }
        return false;
    }
}
