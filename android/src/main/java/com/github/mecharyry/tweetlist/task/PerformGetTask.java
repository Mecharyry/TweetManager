package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;

import com.github.mecharyry.tweetlist.Parser;
import com.github.mecharyry.tweetlist.Request;

import java.lang.ref.WeakReference;

public class PerformGetTask<T, F> extends AsyncTask<String, Void, T> {

    private final Parser<T,F> parser;
    private final WeakReference<Callback> callbackWeakReference;
    private final Request<F> requester;

    public interface Callback<T> {
        void onGetResponse(T tweets);
    }

    public static <T, F> PerformGetTask newInstance(Callback callback, Parser<T, F> parser, Request<F> request) {
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        return new PerformGetTask(callbackWeakReference, parser, request);
    }

    private PerformGetTask(WeakReference<Callback> callbackWeakReference, Parser<T, F> parser, Request<F> requester) {
        this.parser = parser;
        this.callbackWeakReference = callbackWeakReference;
        this.requester = requester;
    }

    @Override
    protected T doInBackground(String... urls) {
        F jsonObject = requester.request(urls[0]);
        return parser.parse(jsonObject);
    }

    @Override
    protected void onPostExecute(T response) {
        super.onPostExecute(response);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onGetResponse(response);
        }
    }

    public void executeTask(String url) {
        this.execute(url);
    }
}
