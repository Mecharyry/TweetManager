package com.github.mecharyry.tweetlist.requester;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends Requester<JSONObject> {

    private static final String TAG = JsonObjectRequest.class.getSimpleName();

    @Override
    JSONObject convertStringTo(String input) {
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e(TAG, "While transforming string to json object.", e);
        }
        return new JSONObject();
    }
}
