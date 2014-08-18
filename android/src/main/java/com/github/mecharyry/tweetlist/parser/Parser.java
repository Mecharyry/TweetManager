package com.github.mecharyry.tweetlist.parser;

import java.io.IOException;

public interface Parser<F, T> {
    T parse(F from);
}
