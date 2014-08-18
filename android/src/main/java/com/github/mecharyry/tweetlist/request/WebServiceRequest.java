package com.github.mecharyry.tweetlist.request;

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

public abstract class WebServiceRequest<T> implements Request<T> {

    private static final String TAG = WebServiceRequest.class.getSimpleName();
    public static final String READING_STREAM_ERROR_MESSAGE = "While reading stream.";

    @Override
    public T request(String signedUrl) throws RequestException {
        Throwable throwable = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(signedUrl);

        try {
            HttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                String inputStreamString = inputStreamToString(inputStream);
                return convertStringTo(inputStreamString);
            }
        } catch (ClientProtocolException e) {
            throwable = e;
        } catch (IOException e) {
            throwable = e;
        }
        throw new RequestException(READING_STREAM_ERROR_MESSAGE, throwable);
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
            Log.e(TAG, READING_STREAM_ERROR_MESSAGE, e);
        }
        return stringBuilder.toString();
    }

    abstract T convertStringTo(String input);

}
