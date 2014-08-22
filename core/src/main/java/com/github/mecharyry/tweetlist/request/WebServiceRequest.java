package com.github.mecharyry.tweetlist.request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public abstract class WebServiceRequest<T> implements Request<T> {

    public static final String READING_STREAM_ERROR_MESSAGE = "While reading stream.";

    @Override
    public T request(String signedUrl) throws RequestException {
        Throwable throwable = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(signedUrl);
        InputStream inputStream = null;

        try {
            HttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                inputStream = entity.getContent();
                return convertStreamTo(inputStream);
            }
        } catch (ClientProtocolException e) {
            throwable = e;
        } catch (IOException e) {
            throwable = e;
        } catch (RequestException e) {
            throwable = e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throwable = e;
                }
            }
        }
        throw RequestException.because(READING_STREAM_ERROR_MESSAGE, throwable);
    }

    protected abstract T convertStreamTo(InputStream inputStream) throws RequestException;
}
