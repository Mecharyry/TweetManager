package com.github.mecharyry;

public class Tweet {
    private String screenName;
    private String location;
    private String text;

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setText(String text) {
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

    @Override
    public String toString() {
        return "Screen Name: " + screenName + " Location: " + location +
                " Text: " + text;
    }
}
