package com.github.mecharyry.tweetlist;

public interface Parser<T, F> {
    T parse(F from);
}
