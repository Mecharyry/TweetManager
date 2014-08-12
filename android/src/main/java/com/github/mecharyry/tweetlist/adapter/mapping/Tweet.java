package com.github.mecharyry.tweetlist.adapter.mapping;

import android.graphics.Bitmap;

public class Tweet {
    private long id;
    private final String screenName;
    private final String location;
    private final String text;
    private final Bitmap thumbImage;

    public Tweet(String screenName, String location, String text, Bitmap thumbImage) {
        this.screenName = screenName;
        this.location = location;
        this.text = text;
        this.thumbImage = thumbImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public Bitmap getThumbImage() {
        return thumbImage;
    }
}
