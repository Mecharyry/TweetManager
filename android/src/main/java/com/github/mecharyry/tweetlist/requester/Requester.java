package com.github.mecharyry.tweetlist.requester;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Requester<T> implements Request<T> {

    private static final String TAG = Requester.class.getSimpleName();

    @Override
    public T request(String signedUrl) throws RequestException {
        Throwable throwable = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(signedUrl);

        try {
            HttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                Log.i(TAG, "Retrieved Response!");
                InputStream inputStream = entity.getContent();
                String inputStreamString = inputStreamToString(inputStream);
                return convertStringTo(inputStreamString);
            }
        } catch (ClientProtocolException e) {
            throwable = e;
        } catch (IOException e) {
            throwable = e;
        }
        throw new RequestException("While reading stream.", throwable);
    }

    protected static String inputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "While reading stream.", e);
        }
        return stringBuilder.toString();
    }

    abstract T convertStringTo(String input);

}
