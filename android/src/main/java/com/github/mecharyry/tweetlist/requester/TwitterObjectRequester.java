package com.github.mecharyry.tweetlist.requester;

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
import java.net.URL;


public class TwitterObjectRequester implements Request<JSONObject> {

    private static final String TAG = "TwitterRequester";

    @Override
    public JSONObject request(URL signedUrl) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(signedUrl.toString());

        try {
            HttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                Log.i(TAG, "Retrieved Response!");
                InputStream inputStream = entity.getContent();
                String inputStreamString = inputStreamToString(inputStream);
                return convertStringToJson(inputStreamString);
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, "While reading stream.", e);
        } catch (IOException e) {
            Log.e(TAG, "While reading stream.", e);
        }

        return new JSONObject();
    }

    private static String inputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "While transforming stream to string.", e);
        }
        return stringBuilder.toString();
    }

    private static JSONObject convertStringToJson(String input) {
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e(TAG, "While transforming string to json object.", e);
        }
        return new JSONObject();
    }


}
