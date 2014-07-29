package com.github.mecharyry.tweetlist;

public interface Request<T> {
    T request(String response);
}
