package com.github.mecharyry;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {
    private Context context;
    private int resource;
    private List<Tweet> tweets;

    public TweetAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.tweets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TweetHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new TweetHolder();
            holder.textText = (TextView) row.findViewById(R.id.text_screen_name);

            row.setTag(holder);
        } else {
            holder = (TweetHolder) row.getTag();
        }

        Tweet tweet = tweets.get(position);
        holder.textText.setText(tweet.getText());

        return row;
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        super.setNotifyOnChange(notifyOnChange);
    }

    private static class TweetHolder {
        TextView textText;
    }
}
