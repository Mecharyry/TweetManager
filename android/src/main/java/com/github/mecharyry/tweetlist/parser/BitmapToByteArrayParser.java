package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapToByteArrayParser implements Parser<Bitmap, byte[]> {
    @Override
    public byte[] parse(Bitmap from) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        from.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
