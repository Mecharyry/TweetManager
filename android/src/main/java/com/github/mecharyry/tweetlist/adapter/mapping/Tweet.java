package com.github.mecharyry.tweetlist.adapter.mapping;

import android.graphics.Bitmap;

public class Tweet {
    private long id;
    private final String screenName;
    private final String location;
    private final String text;
    private final Bitmap thumbImage;
    private final String category;

    public Tweet(long id, String screenName, String location, String text, Bitmap thumbImage, String category) {
        this.id = id;
        this.screenName = screenName;
        this.location = location;
        this.text = text;
        this.thumbImage = thumbImage;
        this.category = category;
    }

    public long getId() {
        return id;
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

    public String getCategory() { return category; }
}
