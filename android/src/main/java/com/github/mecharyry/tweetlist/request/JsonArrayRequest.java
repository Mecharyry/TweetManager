package com.github.mecharyry.tweetlist.request;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayRequest extends Requester<JSONArray> {

    private static final String TAG = JsonArrayRequest.class.getSimpleName();

    @Override
    JSONArray convertStringTo(String input) {
        try {
            return new JSONArray(input);
        } catch (JSONException e) {
            Log.e(TAG, "While converting string to json array.", e);
        }
        return new JSONArray();
    }
}
