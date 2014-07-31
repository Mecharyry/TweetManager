package com.github.mecharyry.tweetlist.requester;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequesterFactory {

    public Request<JSONArray> twitterArrayRequester() {
        return new JsonArrayRequest();
    }

    public Request<JSONObject> twitterObjectRequester() {
        return new JsonObjectRequest();
    }
}
