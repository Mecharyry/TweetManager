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
        try {
            return provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
        } catch (OAuthMessageSignerException e) {
            throwAuthException("While retrieving authentication Url.", e);
        } catch (OAuthNotAuthorizedException e) {
            throwAuthException("While retrieving authentication Url.", e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException("While retrieving authentication Url.", e);
        } catch (OAuthCommunicationException e) {
            throwAuthException("While retrieving authentication Url.", e);
        }
        return null;
    }

    public AccessToken retrieveAccessToken(String oauthVerifier) {
        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
            return new AccessToken(consumer.getToken(), consumer.getTokenSecret());
        } catch (OAuthMessageSignerException e) {
            throwAuthException("While retrieving access token.", e);
        } catch (OAuthNotAuthorizedException e) {
            throwAuthException("While retrieving access token.", e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException("While retrieving access token.", e);
        } catch (OAuthCommunicationException e) {
            throwAuthException("While retrieving access token.", e);
        }
        return null;
    }

    private void throwAuthException(String reason, Exception exception) {
        throw OAuthException.because(reason, exception);
    }

    public static class OAuthException extends RuntimeException {

        private final String reason;
        private final Exception exception;

        private OAuthException(String reason, Exception exception) {
            super(exception);
            this.reason = reason;
            this.exception = exception;
        }

        public static OAuthException because(String reason, Exception exception) {
            return new OAuthException(reason, exception);
        }
    }

}
