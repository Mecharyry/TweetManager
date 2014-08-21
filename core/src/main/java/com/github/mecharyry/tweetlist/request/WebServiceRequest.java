package com.github.mecharyry.tweetlist.request;

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
    public static final String PARSING_STREAM_TO_STRING_ERROR_MESSAGE = "While parsing stream to string";

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
        throw RequestException.because(READING_STREAM_ERROR_MESSAGE, throwable);
    }

    protected static String inputStreamToString(InputStream inputStream) throws RequestException {
        Throwable throwable = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            throwable = e;
        }
        throw RequestException.because(PARSING_STREAM_TO_STRING_ERROR_MESSAGE, throwable);
    }

    abstract T convertStringTo(String input);

}
