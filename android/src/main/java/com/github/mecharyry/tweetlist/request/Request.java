package com.github.mecharyry.tweetlist.request;

public interface Request<T> {
    T request(String request) throws RequestException;
}
