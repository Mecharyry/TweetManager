package com.github.mecharyry.tweetlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class ImageRetriever {
    private static final String TAG = "ImageDownloader";

    public Bitmap retrieveBitmap(String imageUrl) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(imageUrl);

        HttpResponse response;
        try {
            response = client.execute(get);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                Log.i(TAG, "Retrieved Response!");
                InputStream inputStream = entity.getContent();
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException", e);
            throwImageRetrieverException(e);

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            throwImageRetrieverException(e);
        }
        return null;
    }

    private void throwImageRetrieverException(Exception e) {
        throw new ImageRetrieverException(e);
    }

    public static class ImageRetrieverException extends RuntimeException {
        public ImageRetrieverException(Exception e) {
            super(e);
        }
    }
}
