package com.github.mecharyry.tweetlist;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.tweetlist.task.PerformGetTask;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class RequestManager {
    private static final String TAG = "RequestManager";
    private static OAuthConsumer oAuthConsumer;
    private OAuthConsumer consumer;

    public static RequestManager newInstance(AccessToken accessToken) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        oAuthConsumer = oAuthAuthenticator.getConsumer(accessToken);
        return new RequestManager(oAuthConsumer);
    }

    private RequestManager(OAuthConsumer consumer) {
        this.consumer = consumer;
    }

    public void requestAndroidDevTweets(PerformGetTask.Callback callback) {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50"; // TODO: Question: Could we attach this to a particular button rather than having it in code?
        String signedUrl = signUrl(unsignedUrl);
        PerformGetTask.newInstance(callback).execute(signedUrl);
    }

    private String signUrl(String unsignedUrl) {
        String signedUrl = null;
        try {
            signedUrl = consumer.sign(unsignedUrl);
        } catch (OAuthMessageSignerException e) {
            throwAuthException(e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException(e);
        } catch (OAuthCommunicationException e) {
            throwAuthException(e);
        }
        return signedUrl;
    }

    private void throwAuthException(Exception e) {
        throw new OAuthAuthenticator.OAuthException(e);
    }
}
