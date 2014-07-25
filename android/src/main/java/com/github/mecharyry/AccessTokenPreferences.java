package com.github.mecharyry;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.mecharyry.auth.oauth.AccessToken;

public class AccessTokenPreferences {
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_ACCESS_SECRET = "access_secret";
    private final SharedPreferences preferences;
    private String DEFAULT_VALUE = "";

    public static AccessTokenPreferences newInstance(Context context) {
        return new AccessTokenPreferences(context.getSharedPreferences(AccessTokenPreferences.class.getSimpleName(), Context.MODE_PRIVATE));
    }

    private AccessTokenPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveAccessToken(AccessToken accessToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken.getToken());
        editor.putString(PREF_ACCESS_SECRET, accessToken.getSecret());
        editor.apply();
    }

    public AccessToken retrieveAccessToken() {
        String token = preferences.getString(PREF_ACCESS_TOKEN, DEFAULT_VALUE);
        String secret = preferences.getString(PREF_ACCESS_SECRET, DEFAULT_VALUE);
        return new AccessToken(token, secret);
    }

    public boolean hasAccess() {
        return preferences.contains(PREF_ACCESS_TOKEN) && preferences.contains(PREF_ACCESS_SECRET);
    }
}
