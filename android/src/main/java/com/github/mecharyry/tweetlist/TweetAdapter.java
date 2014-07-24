package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mecharyry.R;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {
    private final Context context;
    private final int resource;
    private final List<Tweet> tweets;

    public static TweetAdapter newInstance(Context context, int resource){
        return  new TweetAdapter(context, resource);
    }

    private TweetAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.tweets = new ArrayList<Tweet>();
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets.clear();
        this.tweets.addAll(tweets);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TweetHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

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

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        super.setNotifyOnChange(notifyOnChange);
    }

    private static class TweetHolder {
        TextView textScreenName;
        TextView textTweet;
        TextView textLocation;
    }
}
