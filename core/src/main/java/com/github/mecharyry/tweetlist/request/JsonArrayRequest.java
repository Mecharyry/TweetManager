package com.github.mecharyry.tweetlist.request;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonArrayRequest extends WebServiceRequest<JSONArray> {

    public static final String CONVERTING_JSON_ARRAY_ERROR_MESSAGE = "While converting string to json array.";
    public static final String PARSING_STREAM_TO_STRING_ERROR_MESSAGE = "While parsing stream to string";

    @Override
    protected JSONArray convertStreamTo(InputStream inputStream) throws RequestException {
        Throwable throwable;
        try {
            String inputStreamString = convertStreamToString(inputStream);
            return new JSONArray(inputStreamString);
        } catch (JSONException e) {
            throwable = e;
        } catch (RequestException e) {
            throwable = e;
        }
        throw RequestException.because(CONVERTING_JSON_ARRAY_ERROR_MESSAGE, throwable);
    }

    private String convertStreamToString(InputStream inputStream) throws RequestException {
        Throwable throwable;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throwable = e;
        }
        throw RequestException.because(PARSING_STREAM_TO_STRING_ERROR_MESSAGE, throwable);
    }
}
