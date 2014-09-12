package com.github.mecharyry.tweetlist.parser;

import android.content.ContentValues;
import android.graphics.Bitmap;

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

    public Parser<Bitmap, byte[]> bitmapToByteArrayParser() {
        return new BitmapToByteArrayParser();
    }

    public Parser<byte[], Bitmap> byteArrayToBitmapParser() {
        return new ByteArrayToBitmapParser();
    }
}
