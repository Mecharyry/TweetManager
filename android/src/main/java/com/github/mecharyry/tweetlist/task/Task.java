package com.github.mecharyry.tweetlist.task;

import com.github.mecharyry.tweetlist.parser.Parser;
import com.github.mecharyry.tweetlist.requester.Request;

public class Task<F, T> {

    private final Parser<F, T> parser;
    private final Request<F> requester;
    private final String url;

    public Task(Parser<F, T> parser, Request<F> requester, String url) {
        this.parser = parser;
        this.requester = requester;
        this.url = url;
    }

    public T executeTask(){
        F result = requester.request(url);
        return parser.parse(result);
    }
}
