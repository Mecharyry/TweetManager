package com.github.mecharyry.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private final Notify notify;

    public interface Notify {
        void networkAvailable();

        void networkUnavailable();
    }

    public NetworkChangeReceiver(Notify notify) {
        this.notify = notify;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(NetworkChangeReceiver.class.getSimpleName(), "Context: " + context.toString() + "\nIntent: " + intent);

        if (isNetworkAvailable(context)) {
            notify.networkAvailable();
            return;
        }
        notify.networkUnavailable();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return null != activeNetworkInfo && activeNetworkInfo.isConnectedOrConnecting();
    }
}
