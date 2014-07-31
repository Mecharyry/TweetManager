package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class TaskExecutor {

    public <T> void execute(TaskCompleted<T> callback, Task<?, T> task) {
        InnerTask.newInstance(callback, task).execute();
    }

    static class InnerTask<F, T> extends AsyncTask<Void, Void, T> {

        private final WeakReference<TaskCompleted<T>> callbackWeakReference;
        private final Task<F, T> task;

        static <F, T> InnerTask<F, T> newInstance(TaskCompleted<T> callback, Task<F, T> task) {
            WeakReference<TaskCompleted<T>> callbackWeakReference = new WeakReference<TaskCompleted<T>>(callback);
            return new InnerTask<F, T>(callbackWeakReference, task);
        }

        InnerTask(WeakReference<TaskCompleted<T>> callbackWeakReference, Task<F, T> task) {
            this.callbackWeakReference = callbackWeakReference;
            this.task = task;
        }

        @Override
        protected T doInBackground(Void... params) {
            return task.executeTask();
        }

        @Override
        protected void onPostExecute(T t) {
            super.onPostExecute(t);
            TaskCompleted<T> callback = callbackWeakReference.get();
            if (callback != null) {
                callback.taskCompleted(t);
            }
        }
    }
}
