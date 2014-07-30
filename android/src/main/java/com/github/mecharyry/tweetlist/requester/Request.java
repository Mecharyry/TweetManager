package com.github.mecharyry.tweetlist.requester;

import java.net.URL;

public interface Request<T> {
    T request(URL requestUri);
}
