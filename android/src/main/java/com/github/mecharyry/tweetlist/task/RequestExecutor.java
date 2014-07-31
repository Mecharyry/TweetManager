package com.github.mecharyry.tweetlist.task;

import android.os.AsyncTask;

import com.github.mecharyry.tweetlist.parser.Parser;
import com.github.mecharyry.tweetlist.requester.Request;

import java.lang.ref.WeakReference;

public class RequestExecutor<F, T> extends AsyncTask<String, Void, T> {

    private final Parser<F, T> parser;
    private final WeakReference<Callback> callbackWeakReference;
    private final Request<F> requester;

    public interface Callback<T> {
        void onGetResponse(T tweets);
    }

    public static <F, T> RequestExecutor newInstance(Callback callback, Parser<F, T> parser, Request<F> request) {
        WeakReference<Callback> callbackWeakReference = new WeakReference<Callback>(callback);
        return new RequestExecutor(callbackWeakReference, parser, request);
    }

    RequestExecutor(WeakReference<Callback> callbackWeakReference, Parser<F, T> parser, Request<F> requester) {
        this.parser = parser;
        this.callbackWeakReference = callbackWeakReference;
        this.requester = requester;
    }

    public void executeTask(String url) {
        execute(url);
    }

    @Override
    protected T doInBackground(String... urls) {
        F jsonObject = requester.request(urls[0]);
        return parser.parse(jsonObject);
    }
    ;
    @Override
    protected void onPostExecute(T response) {
        super.onPostExecute(response);
        Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.onGetResponse(response);
        }
    }

}
