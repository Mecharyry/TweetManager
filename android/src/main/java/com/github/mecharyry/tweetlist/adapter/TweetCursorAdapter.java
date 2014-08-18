package com.github.mecharyry.tweetlist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mecharyry.R;
import com.github.mecharyry.db.TweetTable;
import com.github.mecharyry.tweetlist.parser.ByteArrayToBitmapParser;

public class TweetCursorAdapter extends CursorAdapter {

    private static final String TAG = TweetCursorAdapter.class.getSimpleName();
    public static final String ERROR_RETRIEVING_ID_COLUMN = "Error when retrieving column index.";
    private final LayoutInflater layoutInflater;
    private final ByteArrayToBitmapParser parser;

    public static TweetCursorAdapter newInstance(LayoutInflater layoutInflater, Cursor cursor, boolean autoRequery) {
        ByteArrayToBitmapParser parser = new ByteArrayToBitmapParser();
        return new TweetCursorAdapter(layoutInflater, cursor, autoRequery, parser);
    }

    TweetCursorAdapter(LayoutInflater layoutInflater, Cursor cursor, boolean autoRequery, ByteArrayToBitmapParser parser) {
        super(layoutInflater.getContext(), cursor, autoRequery);
        this.parser = parser;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.tweets_list_item, parent, false);

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
        int locationColumnIndex = cursor.getColumnIndex(TweetTable.COLUMNS.COLUMN_LOCATION.getColumnHeader());
        int screenNameColumnIndex = cursor.getColumnIndex(TweetTable.COLUMNS.COLUMN_SCREEN_NAME.getColumnHeader());
        int textColumnIndex = cursor.getColumnIndex(TweetTable.COLUMNS.COLUMN_TWEET_TEXT.getColumnHeader());
        int bitmapColumnIndex = cursor.getColumnIndex(TweetTable.COLUMNS.COLUMN_THUMB_IMAGE.getColumnHeader());

        String location = cursor.getString(locationColumnIndex);
        String screenName = cursor.getString(screenNameColumnIndex);
        String text = cursor.getString(textColumnIndex);
        byte[] imageData = cursor.getBlob(bitmapColumnIndex);

        Bitmap bitmap = parser.parse(imageData);

        TweetHolder holder = (TweetHolder) view.getTag();
        if (holder != null) {
            holder.textScreenName.setText(screenName);
            holder.textLocation.setText(location);
            holder.textTweet.setText(text);
            holder.imageThumbnail.setImageBitmap(bitmap);
        }
    }

    public long getFinalItemId() {
        try {
            int finalItemIndex = getCount() - 1;
            Cursor cursor = (Cursor) getItem(finalItemIndex);
            int columnIdIndex = cursor.getColumnIndexOrThrow(TweetTable.COLUMNS.COLUMN_ID.getColumnHeader());
            return cursor.getLong(columnIdIndex);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, ERROR_RETRIEVING_ID_COLUMN);
        }
        return -1;
    }

    private static class TweetHolder {
        TextView textScreenName;
        TextView textTweet;
        TextView textLocation;
        ImageView imageThumbnail;
    }
}
