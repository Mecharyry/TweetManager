package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;

import com.github.mecharyry.DeveloperError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToByteArrayParser implements Parser<Bitmap, byte[]> {

    private static final String ERROR_CLOSING_STREAM_MESSAGE = "Error Closing Stream.";

    @Override
    public byte[] parse(Bitmap from) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            from.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            byte[] result = outputStream.toByteArray();
            outputStream.close();
            return result;
        } catch (IOException e) {
            throw DeveloperError.because(ERROR_CLOSING_STREAM_MESSAGE, e);
        }
    }
}
