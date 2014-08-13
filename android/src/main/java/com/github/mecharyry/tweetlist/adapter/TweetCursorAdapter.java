package com.github.mecharyry.tweetlist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mecharyry.R;
import com.github.mecharyry.db.TweetTable;
import com.github.mecharyry.tweetlist.parser.ParserFactory;

public class TweetCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private final ParserFactory parserFactory;
    private final int locationColumnIndex;
    private final int screenNameColumnIndex;
    private final int textColumnIndex;
    private final int bitmapColumnIndex;

    public static TweetCursorAdapter newInstance(Context context, Cursor cursor, boolean autoRequery) {
        ParserFactory parserFactory = ParserFactory.newInstance();
        return new TweetCursorAdapter(context, cursor, autoRequery, parserFactory);
    }

    TweetCursorAdapter(Context context, Cursor cursor, boolean autoRequery, ParserFactory parserFactory) {
        super(context, cursor, autoRequery);
        this.parserFactory = parserFactory;
        inflater = LayoutInflater.from(context);

        locationColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_LOCATION);
        screenNameColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_SCREEN_NAME);
        textColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_TWEET_TEXT);
        bitmapColumnIndex = cursor.getColumnIndex(TweetTable.COLUMN_THUMB_IMAGE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.tweets_list_item, parent, false);

        TweetHolder holder = new TweetHolder();
        holder.textScreenName = (TextView) view.findViewById(R.id.text_screen_name);
        holder.textTweet = (TextView) view.findViewById(R.id.text_tweet);
        holder.textLocation = (TextView) view.findViewById(R.id.text_location);
        holder.imageThumbnail = (ImageView) view.findViewById(R.id.user_thumb_image);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String location = cursor.getString(locationColumnIndex);
        String screenName = cursor.getString(screenNameColumnIndex);
        String text = cursor.getString(textColumnIndex);
        byte[] imageData = cursor.getBlob(bitmapColumnIndex);
        Bitmap bitmap = parserFactory.byteArrayToBitmapParser().parse(imageData);

        TweetHolder holder = (TweetHolder) view.getTag();
        if (holder != null) {
            holder.textScreenName.setText(screenName);
            holder.textLocation.setText(location);
            holder.textTweet.setText(text);
            holder.imageThumbnail.setImageBitmap(bitmap);
        }
    }

    private static class TweetHolder {
        TextView textScreenName;
        TextView textTweet;
        TextView textLocation;
        ImageView imageThumbnail;
    }
}
