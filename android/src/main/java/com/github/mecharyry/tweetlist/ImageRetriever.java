package com.github.mecharyry.tweetlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.mecharyry.tweetlist.request.RequestException;
import com.github.mecharyry.tweetlist.request.WebServiceRequest;

import java.io.InputStream;

public class ImageRetriever extends WebServiceRequest<Bitmap> {

    public static final String ERROR_READING_STREAM_MESSAGE = "While reading bitmap stream.";

    @Override
    protected Bitmap convertStreamTo(InputStream inputStream) throws RequestException {
        Throwable throwable;
        try {
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            throwable = e;
        }
        throw RequestException.because(ERROR_READING_STREAM_MESSAGE, throwable);

    }
}
