package com.github.mecharyry.tweetlist.request;

import com.github.mecharyry.DeveloperError;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends WebServiceRequest<JSONObject> {

    public static final String CONVERTING_JSON_OBJECT_ERROR_MESSAGE = "While transforming string to json object.";

    @Override
    JSONObject convertStringTo(String input) {
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            throw DeveloperError.because(CONVERTING_JSON_OBJECT_ERROR_MESSAGE, e);
        }
    }
}
