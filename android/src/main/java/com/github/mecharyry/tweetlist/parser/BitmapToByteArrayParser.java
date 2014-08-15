package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToByteArrayParser implements Parser<Bitmap, byte[]> {

    private static final String TAG = BitmapToByteArrayParser.class.getSimpleName();
    private static final String ERROR_CLOSING_STREAM_MESSAGE = "Error Closing Stream.";

    @Override
    public byte[] parse(Bitmap from) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            from.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            byte[] result = outputStream.toByteArray();
            return result;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e(TAG, ERROR_CLOSING_STREAM_MESSAGE, e);
            }
        }
    }
}
