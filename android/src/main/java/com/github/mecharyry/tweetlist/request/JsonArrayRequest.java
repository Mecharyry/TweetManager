package com.github.mecharyry.tweetlist.request;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayRequest extends Requester<JSONArray> {

    private static final String TAG = JsonArrayRequest.class.getSimpleName();
    public static final String CONVERTING_JSON_ARRAY_ERROR_MESSAGE = "While converting string to json array.";

    @Override
    JSONArray convertStringTo(String input) {
        try {
            return new JSONArray(input);
        } catch (JSONException e) {
            Log.e(TAG, CONVERTING_JSON_ARRAY_ERROR_MESSAGE, e);
        }
        return new JSONArray();
    }
}
