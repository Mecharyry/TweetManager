package com.github.mecharyry.tweetlist.factory;


import com.github.mecharyry.tweetlist.ImageRetriever;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.parser.Parser;
import com.github.mecharyry.tweetlist.parser.TweetsHashtagParser;
import com.github.mecharyry.tweetlist.parser.TweetsMyStreamParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ParserFactory {

    private ImageRetriever imageRetriever;

    public static ParserFactory newInstance(){
        ImageRetriever imageRetriever = new ImageRetriever();
        return new ParserFactory(imageRetriever);
    }

    ParserFactory(ImageRetriever imageRetriever) {
        this.imageRetriever = imageRetriever;
    }

    public Parser<JSONArray, List<Tweet>> myStreamParser(){
        return new TweetsMyStreamParser(imageRetriever);
    }

    public Parser<JSONObject, List<Tweet>> hashtagParser(){
        return new TweetsHashtagParser(imageRetriever);
    }
}
