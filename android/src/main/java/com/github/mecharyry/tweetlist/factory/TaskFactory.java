package com.github.mecharyry.tweetlist.factory;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.requester.TwitterArrayRequester;
import com.github.mecharyry.tweetlist.requester.TwitterObjectRequester;
import com.github.mecharyry.tweetlist.task.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class TaskFactory {
    public static final String ANDROID_DEV_TWEETS = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
    public static final String MY_STREAM_TWEETS = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=50";
    private final OAuthConsumer consumer;
    private final ParserFactory parserFactory;
    private final TwitterObjectRequester objectRequester;
    private final TwitterArrayRequester arrayRequester;


    public static TaskFactory newInstance(AccessToken accessToken) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        OAuthConsumer oAuthConsumer = oAuthAuthenticator.getConsumer(accessToken);
        TwitterObjectRequester objectRequester = new TwitterObjectRequester();
        TwitterArrayRequester arrayRequester = new TwitterArrayRequester();
        ParserFactory parserFactory = ParserFactory.newInstance();
        return new TaskFactory(oAuthConsumer, parserFactory, arrayRequester, objectRequester);
    }

    TaskFactory(OAuthConsumer consumer, ParserFactory parserFactory, TwitterArrayRequester arrayRequester, TwitterObjectRequester objectRequester) {
        this.consumer = consumer;
        this.parserFactory = parserFactory;
        this.objectRequester = objectRequester;
        this.arrayRequester = arrayRequester;
    }

    public Task<JSONObject, List<Tweet>> requestAndroidDevTweets() {
        String signedUrl = signUrl(ANDROID_DEV_TWEETS);
        return new Task<JSONObject, List<Tweet>>(parserFactory.hashtagParser(), objectRequester, signedUrl);
    }

    public Task<JSONArray, List<Tweet>> requestMyStreamTweets() {
        String signedUrl = signUrl(MY_STREAM_TWEETS);
        return new Task<JSONArray, List<Tweet>>(parserFactory.myStreamParser(), arrayRequester, signedUrl);
    }

    private String signUrl(String unsignedUrl) {
        String signedUrl = null;
        try {
            signedUrl = consumer.sign(unsignedUrl);
        } catch (OAuthMessageSignerException e) {
            throwAuthException("While signing URL.", e);
        } catch (OAuthExpectationFailedException e) {
            throwAuthException("While signing URL.", e);
        } catch (OAuthCommunicationException e) {
            throwAuthException("While signing URL.", e);
        }
        return signedUrl;
    }

    private void throwAuthException(String reason, Exception exception) {
        throw OAuthAuthenticator.OAuthException.because(reason, exception);
    }
}
