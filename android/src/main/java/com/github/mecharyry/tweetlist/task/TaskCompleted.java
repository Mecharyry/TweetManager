package com.github.mecharyry.tweetlist.task;

public interface TaskCompleted<T> {
    void taskCompleted(T response);
}
