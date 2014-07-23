package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.mecharyry.tweetlist.task.RetrieveTweetsByHashtagTask;
import com.github.mecharyry.tweetlist.Tweet;
import com.github.mecharyry.tweetlist.TweetAdapter;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class ListViewActivity extends Activity {

    private static final String TAG = "ListViewActivity";
    private ArrayList<Tweet> tweets;
    private TweetAdapter tweetArrayAdapter;
    private OAuthConsumer consumer;
    private ListView listView;

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets.clear();
        this.tweets.addAll(tweets);
        tweetArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        int layoutId = R.layout.tweets_list_item;
        listView = (ListView) findViewById(R.id.listview_tweets);

        consumer = (OAuthConsumer) getIntent().getExtras().get("CONSUMER");
        tweets = new ArrayList<Tweet>();

        tweetArrayAdapter = new TweetAdapter(this, layoutId, tweets);
        listView.setAdapter(tweetArrayAdapter);

        requestAndroidDevTweets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestAndroidDevTweets() {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
        String signedUrl = signUrl(unsignedUrl);
        Log.i(TAG, "URL: " + signedUrl);
        new RetrieveTweetsByHashtagTask(this).execute(signedUrl);
    }

    private String signUrl(String unsignedUrl) {
        if (unsignedUrl != null &&
                unsignedUrl != "") {
            try {
                return consumer.sign(unsignedUrl);
            } catch (OAuthMessageSignerException e) {
                Log.e(TAG, "OAuthMessageSignerException", e);
            } catch (OAuthExpectationFailedException e) {
                Log.e(TAG, "OAuthExpectationFailedException", e);
            } catch (OAuthCommunicationException e) {
                Log.e(TAG, "OAuthCommunicationException", e);
            }
        }
        return null;
    }
}
