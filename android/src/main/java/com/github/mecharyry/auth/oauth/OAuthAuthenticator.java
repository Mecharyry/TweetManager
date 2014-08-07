package com.github.mecharyry.auth.oauth;

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
            throw new DeveloperError();
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

    public String retrieveAuthenticationUrl() {
        Throwable throwable;
        try {
            return provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
        } catch (OAuthMessageSignerException e) {
            throwable = e;
        } catch (OAuthNotAuthorizedException e) {
            throwable = e;
        } catch (OAuthExpectationFailedException e) {
            throwable = e;
        } catch (OAuthCommunicationException e) {
            throwable = e;
        }
        // TODO: Handle network retry.
        return "ERROR";
    }

    public AccessToken retrieveAccessToken(String oauthVerifier) {
        Throwable throwable;
        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
            return new AccessToken(consumer.getToken(), consumer.getTokenSecret());
        } catch (OAuthMessageSignerException e) {
            throwable = e;
        } catch (OAuthNotAuthorizedException e) {
            throwable = e;
        } catch (OAuthExpectationFailedException e) {
            throwable = e;
        } catch (OAuthCommunicationException e) {
            throwable = e;
        }
        throw OAuthException.because("While retrieving access token.", throwable);
    }

    public static class OAuthException extends RuntimeException {

        private final String reason;
        private final Throwable throwable;

        public static OAuthException because(String reason, Throwable throwable) {
            return new OAuthException(reason, throwable);
        }

        private OAuthException(String reason, Throwable throwable) {
            super(throwable);
            this.reason = reason;
            this.throwable = throwable;
        }
    }
}
