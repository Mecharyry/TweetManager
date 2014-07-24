package com.github.mecharyry.auth.oauth;

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
    private static final String CONSUMER_KEY = "Mz3VVcNtAX7m1UIi2tS8Xf8X9";
    private static final String CONSUMER_SECRET = "Lj5tHjg5sl2OXHrfjzBI9yTc86wIs7PN4MUJPOUo7076vdciiH";
    private final OAuthConsumer consumer;
    private final OAuthProvider provider;


    public static OAuthAuthenticator newInstance() {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        OAuthProvider provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_ENDPOINT_URL, ACCESS_TOKEN_ENDPOINT_URL, AUTHORIZATION_WEBSITE_URL);
        return new OAuthAuthenticator(consumer, provider);
    }

    public OAuthConsumer getConsumer(AccessToken accessToken) {
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
        return consumer;
    }

    public OAuthAuthenticator(OAuthConsumer consumer, OAuthProvider provider) {
        this.consumer = consumer;
        this.provider = provider;
    }

    public String retrieveAuthenticationUrl() {
        try {
            return provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
        } catch (OAuthMessageSignerException e) {
            throwAuthException(e);
        } catch (OAuthNotAuthorizedException e) {
            throwAuthException(e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException(e);
        } catch (OAuthCommunicationException e) {
            throwAuthException(e);
        }
        return null;
    }

    public AccessToken retrieveAccessToken(String oauthVerifier) {
        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
            return new AccessToken(consumer.getToken(), consumer.getTokenSecret());
        } catch (OAuthMessageSignerException e) {
            throwAuthException(e);
        } catch (OAuthNotAuthorizedException e) {
            throwAuthException(e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException(e);
        } catch (OAuthCommunicationException e) {
            throwAuthException(e);
        }
        return null;
    }

    private void throwAuthException(Exception e) {
        throw new OAuthException(e);
    }

    public static class OAuthException extends RuntimeException {
        public OAuthException(Exception e) {
            super(e);
        }
    }
}
