package com.github.mecharyry.tweetlist;

import com.github.mecharyry.auth.oauth.AccessToken;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;
import com.github.mecharyry.tweetlist.adapter.mapping.Tweet;
import com.github.mecharyry.tweetlist.parser.Parser;
import com.github.mecharyry.tweetlist.parser.TweetsHashTagParser;
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

public class RequestManager {
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

    public void requestAndroidDevTweets(PerformGetTask.Callback<List<Tweet>> callback) {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
        String signedUrl = signUrl(unsignedUrl);
        Parser<List<Tweet>, JSONObject> parser = new TweetsHashTagParser(new ImageRetriever());
        TwitterObjectRequester requester = new TwitterObjectRequester();
        PerformGetTask.newInstance(callback, parser, requester).executeTask(signedUrl);
    }

    public void requestMyStreamTweets(PerformGetTask.Callback<List<Tweet>> callback) {
        String unsignedUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=50";
        String signedUrl = signUrl(unsignedUrl);
        Parser<List<Tweet>, JSONArray> parser = new TweetsMyStreamParser(new ImageRetriever());
        TwitterArrayRequester requester = new TwitterArrayRequester();
        PerformGetTask.newInstance(callback, parser, requester).executeTask(signedUrl);
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
