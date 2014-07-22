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

public class RetrieveTweetsByHashtagTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "TwitterRequest";
    private final ListViewActivity context;

    public RetrieveTweetsByHashtagTask(ListViewActivity context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(params[0]);

        HttpResponse response;
        try {
            response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                Log.i(TAG, "SUCCESS!");
                InputStream inputStream = entity.getContent();

                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                    return convertStringToJson(stringBuilder.toString());
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        context.displayTweets(jsonObject);

        super.onPostExecute(jsonObject);
    }

    private static JSONObject convertStringToJson(String input) {
        try {
            Log.i(TAG, new JSONObject(input).toString());
            return new JSONObject(input);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
