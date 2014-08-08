package com.github.mecharyry.auth.oauth;

import android.util.Log;

import com.github.mecharyry.BuildConfig;
import com.github.mecharyry.DeveloperError;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class OAuthAuthenticator {

    private static final String TAG = OAuthAuthenticator.class.getSimpleName();
    private static final String OAUTH_CALLBACK_URL = "mecharyry-android:///";
    private static final String REQUEST_TOKEN_ENDPOINT_URL = "https://api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT_URL = "https://api.twitter.com/oauth/access_token";
    private static final String AUTHORIZATION_WEBSITE_URL = "https://api.twitter.com/oauth/authorize";
    private static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    private static final String CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

    private final OAuthConsumer consumer;
    private final OAuthProvider provider;

    static {
        if (CONSUMER_KEY.isEmpty() || CONSUMER_SECRET.isEmpty()) {
            throw DeveloperError.because("Consumer Key / Secret not present", new Throwable());
        }
    }

    public static OAuthAuthenticator newInstance() {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        OAuthProvider provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_ENDPOINT_URL, ACCESS_TOKEN_ENDPOINT_URL, AUTHORIZATION_WEBSITE_URL);
        return new OAuthAuthenticator(consumer, provider);
    }

    OAuthAuthenticator(OAuthConsumer consumer, OAuthProvider provider) {
        this.consumer = consumer;
        this.provider = provider;
    }

    public OAuthConsumer getConsumer(AccessToken accessToken) {
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
        return consumer;
    }

    public NetworkResponse retrieveAuthenticationUrl() {
        String response;
        try {
            response = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
            return new NetworkResponse(RequestStatus.REQUEST_SUCCESS, response);
        } catch (OAuthMessageSignerException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthNotAuthorizedException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthExpectationFailedException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthCommunicationException e) {
            Log.e(TAG, e.getMessage());
        }
        response = "Network Error. Please try again.";
        return new NetworkResponse(RequestStatus.REQUEST_FAILED, response);
    }

    public NetworkResponse retrieveAccessToken(String oauthVerifier) {
        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
            AccessToken accessToken = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
            return new NetworkResponse(RequestStatus.REQUEST_SUCCESS, accessToken);
        } catch (OAuthMessageSignerException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthNotAuthorizedException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthExpectationFailedException e) {
            Log.e(TAG, e.getMessage());
        } catch (OAuthCommunicationException e) {
            Log.e(TAG, e.getMessage());
        }
        String response = "Network Error. Please try again.";
        return new NetworkResponse(RequestStatus.REQUEST_FAILED, response);
    }
}
