package com.github.mecharyry.tweetlist.adapter.mapping;

public class Tweet {
    private final String screenName;
    private final String location;
    private final String text;

    public Tweet(String screenName, String location, String text) {
        this.screenName = screenName;
        this.location = location;
        this.text = text;
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
}
