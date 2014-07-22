package com.github.mecharyry;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TwitterRequestTask extends AsyncTask<String, Void, ArrayList<?>> {
    private static final String TAG = "TwitterRequestTask";

    protected InputStream performGet(String... urls) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);

        HttpResponse response;
        try {
            response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                Log.i(TAG, "Retrieved Response!");
                return entity.getContent();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    protected static JSONObject convertStringToJson(String input) {
        try {
            Log.i(TAG, new JSONObject(input).toString());
            return new JSONObject(input);
        } catch (JSONException e) {
            e.printStackTrace();    // TODO: Handle Exception.
        }
        return null;
    }

    @Override
    protected ArrayList<?> doInBackground(String... params) {
        throw new UnsupportedOperationException("Please use the subclass implementation of the doInBackground method.");
    }
}
