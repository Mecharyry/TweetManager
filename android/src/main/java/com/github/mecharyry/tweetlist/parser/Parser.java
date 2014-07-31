package com.github.mecharyry.tweetlist.parser;

public interface Parser<F, T> {
    T parse(F from);
}
