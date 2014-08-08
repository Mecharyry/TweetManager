package com.github.mecharyry.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private final Callback callback;

    public interface Callback {
        void networkAvailable();

        void networkUnavailable();
    }

    public NetworkChangeReceiver(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(NetworkChangeReceiver.class.getSimpleName(), "Context: " + context.toString() + "\nIntent: " + intent);

        if (isNetworkAvailable(context)) {
            callback.networkAvailable();
            return;
        }
        callback.networkUnavailable();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return null != activeNetworkInfo && activeNetworkInfo.isConnectedOrConnecting();
    }
}
