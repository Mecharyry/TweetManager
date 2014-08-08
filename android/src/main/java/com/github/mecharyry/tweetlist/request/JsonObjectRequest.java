package com.github.mecharyry.tweetlist.request;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends Requester<JSONObject> {

    private static final String TAG = JsonObjectRequest.class.getSimpleName();
    public static final String CONVERTING_JSON_OBJECT_ERROR_MESSAGE = "While transforming string to json object.";

    @Override
    JSONObject convertStringTo(String input) {
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e(TAG, CONVERTING_JSON_OBJECT_ERROR_MESSAGE, e);
        }
        return new JSONObject();
    }
}
