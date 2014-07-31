package com.github.mecharyry.tweetlist.factory;

import com.github.mecharyry.tweetlist.requester.Request;
import com.github.mecharyry.tweetlist.requester.TwitterArrayRequester;
import com.github.mecharyry.tweetlist.requester.TwitterObjectRequester;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequesterFactory {

    public Request<JSONArray> twitterArrayRequester(){
        return new TwitterArrayRequester();
    }

    public Request<JSONObject> twitterObjectRequester(){
        return new TwitterObjectRequester();
    }
}
