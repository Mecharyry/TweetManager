package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class TaskExecutor {

    public <T> void execute(TaskCompletion<T> callback, Task<?, T> task) {
        InnerTask.newInstance(callback, task).execute();
    }

    static class InnerTask<F, T> extends AsyncTask<Void, Void, T> {

        private final WeakReference<TaskCompletion<T>> callbackWeakReference;
        private final Task<F, T> task;

        static <F, T> InnerTask<F, T> newInstance(TaskCompletion<T> callback, Task<F, T> task) {
            WeakReference<TaskCompletion<T>> callbackWeakReference = new WeakReference<TaskCompletion<T>>(callback);
            return new InnerTask<F, T>(callbackWeakReference, task);
        }

        InnerTask(WeakReference<TaskCompletion<T>> callbackWeakReference, Task<F, T> task) {
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
            TaskCompletion<T> callback = callbackWeakReference.get();
            if (callback != null) {
                callback.taskCompleted(t);
            }
        }
    }
}
