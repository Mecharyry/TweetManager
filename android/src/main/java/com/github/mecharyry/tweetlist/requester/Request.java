package com.github.mecharyry.tweetlist.requester;

public interface Request<T> {
    T request(String request);
}
