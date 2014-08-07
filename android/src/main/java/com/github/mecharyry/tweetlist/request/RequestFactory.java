package com.github.mecharyry.tweetlist.request;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestFactory {

    public Request<JSONArray> twitterArrayRequest() {
        return new JsonArrayRequest();
    }

    public Request<JSONObject> twitterObjectRequest() {
        return new JsonObjectRequest();
    }
}
