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
    public static final String NETWORK_ERROR_MESSAGE = "Network Error. Please try again.";

    private final OAuthConsumer consumer;
    private final OAuthProvider provider;

    public static OAuthAuthenticator newInstance(String consumerKey, String consumerSecret) {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
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

    public NetworkResponse<String> retrieveAuthenticationUrl() {
        try {
            String response = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
            return new NetworkResponse<String>(RequestStatus.REQUEST_SUCCESS, response);
        } catch (OAuthMessageSignerException e) {

        } catch (OAuthNotAuthorizedException e) {

        } catch (OAuthExpectationFailedException e) {

        } catch (OAuthCommunicationException e) {

        }
        return new NetworkResponse<String>(RequestStatus.REQUEST_FAILED, NETWORK_ERROR_MESSAGE);
    }

    public NetworkResponse retrieveAccessToken(String oauthVerifier) {
        try {
            provider.retrieveAccessToken(consumer, oauthVerifier);
            AccessToken accessToken = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
            return new NetworkResponse<AccessToken>(RequestStatus.REQUEST_SUCCESS, accessToken);
        } catch (OAuthMessageSignerException e) {

        } catch (OAuthNotAuthorizedException e) {

        } catch (OAuthExpectationFailedException e) {

        } catch (OAuthCommunicationException e) {

        }
        return new NetworkResponse<String>(RequestStatus.REQUEST_FAILED, NETWORK_ERROR_MESSAGE);
    }
}
