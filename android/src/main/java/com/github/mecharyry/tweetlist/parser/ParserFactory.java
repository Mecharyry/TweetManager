package com.github.mecharyry.tweetlist.parser;


import android.content.ContentValues;

import com.github.mecharyry.tweetlist.ImageRetriever;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParserFactory {

    private ImageRetriever imageRetriever;

    public static ParserFactory newInstance() {
        ImageRetriever imageRetriever = new ImageRetriever();
        return new ParserFactory(imageRetriever);
    }

    ParserFactory(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    public Parser<JSONArray, ContentValues[]> myStreamParser() {
        return MyStreamToContentValuesParser.newInstance(imageRetriever);
    }

    public Parser<JSONObject, ContentValues[]> hashtagParser() {
        return HashtagToContentValuesParser.newInstance(imageRetriever);
    }
}
