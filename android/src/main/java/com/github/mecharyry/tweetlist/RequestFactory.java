package com.github.mecharyry.tweetlist;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.parser.Parser;
import com.github.mecharyry.tweetlist.parser.TweetsHashtagParser;
import com.github.mecharyry.tweetlist.parser.TweetsMyStreamParser;
import com.github.mecharyry.tweetlist.requester.TwitterArrayRequester;
import com.github.mecharyry.tweetlist.requester.TwitterObjectRequester;
import com.github.mecharyry.tweetlist.task.PerformGetTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class RequestFactory {
    public static final String ANDROID_DEV_TWEETS = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
    public static final String MY_STREAM_TWEETS = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=50";
    private final OAuthConsumer consumer;
    private final Parser<List<Tweet>, JSONObject> hashtagParser;
    private final Parser<List<Tweet>, JSONArray> myStreamParser;
    private final TwitterObjectRequester objectRequester;
    private final TwitterArrayRequester arrayRequester;


    public static RequestFactory newInstance(AccessToken accessToken) {
        OAuthAuthenticator oAuthAuthenticator = OAuthAuthenticator.newInstance();
        OAuthConsumer oAuthConsumer = oAuthAuthenticator.getConsumer(accessToken);
        ImageRetriever imageRetriever = new ImageRetriever();
        Parser<List<Tweet>, JSONObject> hashtagParser = new TweetsHashtagParser(imageRetriever);
        Parser<List<Tweet>, JSONArray> myStreamParser = new TweetsMyStreamParser(imageRetriever);
        TwitterObjectRequester objectRequester = new TwitterObjectRequester();
        TwitterArrayRequester arrayRequester = new TwitterArrayRequester();
        return new RequestFactory(oAuthConsumer, myStreamParser, arrayRequester, hashtagParser, objectRequester);
    }

    RequestFactory(OAuthConsumer consumer, Parser<List<Tweet>, JSONArray> myStreamParser,
                           TwitterArrayRequester arrayRequester, Parser<List<Tweet>, JSONObject> hashtagParser, TwitterObjectRequester objectRequester) {
        this.consumer = consumer;
        this.hashtagParser = hashtagParser;
        this.myStreamParser = myStreamParser;
        this.objectRequester = objectRequester;
        this.arrayRequester = arrayRequester;
    }

    public void requestAndroidDevTweets(PerformGetTask.Callback<List<Tweet>> callback) {
        String signedUrl = signUrl(ANDROID_DEV_TWEETS);
        PerformGetTask.newInstance(callback, hashtagParser, objectRequester).executeTask(signedUrl);
    }

    public void requestMyStreamTweets(PerformGetTask.Callback<List<Tweet>> callback) {
        String signedUrl = signUrl(MY_STREAM_TWEETS);
        PerformGetTask.newInstance(callback, myStreamParser, arrayRequester).executeTask(signedUrl);
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
