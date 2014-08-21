package com.github.mecharyry.tweetlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

// TODO: Repurpose to use web request.
public class ImageRetriever {

    public static final String ERROR_READING_STREAM_MESSAGE = "While reading bitmap stream.";

    public Bitmap retrieveBitmap(String imageUrl) {
        Throwable throwable = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(imageUrl);

        HttpResponse response;
        try {
            response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (ClientProtocolException e) {
            throwable = e;
        } catch (IOException e) {
            throwable = e;
        }
        throw ImageRetrieverException.because(ERROR_READING_STREAM_MESSAGE, throwable);
    }

    public static class ImageRetrieverException extends RuntimeException {

        public static ImageRetrieverException because(String reason, Throwable throwable) {
            return new ImageRetrieverException(reason, throwable);
        }

        private ImageRetrieverException(String reason, Throwable throwable) {
            super(reason, throwable);
        }
    }
}
