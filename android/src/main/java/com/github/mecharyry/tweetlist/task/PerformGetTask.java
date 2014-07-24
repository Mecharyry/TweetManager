package com.github.mecharyry.tweetlist.task;

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
import java.lang.ref.WeakReference;

public class PerformGetTask extends AsyncTask<String, Void, InputStream> {
    private static String TAG = "PerformGetTask";
    private final WeakReference<Callback> callbackWeakReference;

    public interface Callback {
        void onRetrieved(InputStream stream);
    }

    public PerformGetTask(Callback callback) {
        callbackWeakReference = new WeakReference<Callback>(callback);
    }

    @Override
    protected InputStream doInBackground(String... urls) {
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
            Log.e(TAG, "ClientProtocolException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onRetrieved(inputStream);
        }
    }

    protected static String inputStreamToString(InputStream inputStream) throws IOException { // TODO: Move to new class.
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    protected static JSONObject convertStringToJson(String input) { // TODO: Move to new class.
        try {
            Log.i(TAG, new JSONObject(input).toString());
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }
}
