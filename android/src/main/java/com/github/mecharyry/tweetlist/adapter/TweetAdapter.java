package com.github.mecharyry.tweetlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mecharyry.R;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private final List<Tweet> tweets;

    public static TweetAdapter newInstance(Context context) {
        return new TweetAdapter(LayoutInflater.from(context), new ArrayList<Tweet>());
    }

    private TweetAdapter(LayoutInflater layoutInflater, List<Tweet> tweets) {
        this.layoutInflater = layoutInflater;
        this.tweets = tweets;
    }

    public void updateTweets(List<Tweet> tweets) {
        this.tweets.clear();
        this.tweets.addAll(tweets);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tweets.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TweetHolder holder = null;

        if (row == null) {
            row = layoutInflater.inflate(R.layout.tweets_list_item, parent, false);

            holder = new TweetHolder();
            holder.textScreenName = (TextView) row.findViewById(R.id.text_screen_name);
            holder.textTweet = (TextView) row.findViewById(R.id.text_tweet);
            holder.textLocation = (TextView) row.findViewById(R.id.text_location);

            row.setTag(holder);
        } else {
            holder = (TweetHolder) row.getTag();
        }

        Tweet tweet = tweets.get(position);
        holder.textScreenName.setText("Screen Name: " + tweet.getScreenName());
        holder.textTweet.setText(tweet.getText());
        holder.textLocation.setText("Location: " + tweet.getLocation());

        return row;
    }

    private static class TweetHolder {
        TextView textScreenName;
        TextView textTweet;
        TextView textLocation;
    }
}
