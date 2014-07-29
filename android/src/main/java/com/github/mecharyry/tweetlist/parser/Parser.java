package com.github.mecharyry.tweetlist.parser;

public interface Parser<T, F> {
    T parse(F from);
}
