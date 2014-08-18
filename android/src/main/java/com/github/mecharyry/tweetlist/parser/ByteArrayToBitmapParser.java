package com.github.mecharyry.tweetlist.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ByteArrayToBitmapParser implements Parser<byte[], Bitmap> {
    @Override
    public Bitmap parse(byte[] from) {
        return BitmapFactory.decodeByteArray(from, 0, from.length);
    }
}
