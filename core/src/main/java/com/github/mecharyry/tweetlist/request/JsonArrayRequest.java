package com.github.mecharyry.tweetlist.request;

import com.github.mecharyry.DeveloperError;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayRequest extends WebServiceRequest<JSONArray> {

    public static final String CONVERTING_JSON_ARRAY_ERROR_MESSAGE = "While converting string to json array.";

    @Override
    JSONArray convertStringTo(String input) {
        try {
            return new JSONArray(input);
        } catch (JSONException e) {
            throw DeveloperError.because(CONVERTING_JSON_ARRAY_ERROR_MESSAGE, e);
        }
    }
}
