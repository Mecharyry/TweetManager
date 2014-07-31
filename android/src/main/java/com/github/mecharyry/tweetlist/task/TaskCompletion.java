package com.github.mecharyry.tweetlist.task;

public interface TaskCompletion<T> {
    void taskCompleted(T response);
}
