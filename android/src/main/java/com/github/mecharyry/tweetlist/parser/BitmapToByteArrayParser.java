package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToByteArrayParser implements Parser<Bitmap, byte[]> {
    @Override
    public byte[] parse(Bitmap from) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            from.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            byte[] result = outputStream.toByteArray();
            return result;
        } finally {
            outputStream.close();
        }
    }
}
