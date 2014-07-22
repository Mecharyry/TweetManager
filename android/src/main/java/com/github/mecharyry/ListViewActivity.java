package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class ListViewActivity extends Activity {

    private static final String TAG = "ListViewActivity";
    private static final String TAG_STATUSES = "statuses";
    private static final String TAG_TEXT = "text";
    private static final String TAG_USER = "user";
    private static final String TAG_SCREEN_NAME = "screen_name";
    private static final String TAG_LOCATION = "location";
    private ArrayList<Tweet> tweets;
    private TweetAdapter tweetArrayAdapter;
    private OAuthConsumer consumer;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        int layoutId = R.layout.tweets_list_item;
        listView = (ListView) findViewById(R.id.listview_tweets);

        consumer = (OAuthConsumer) getIntent().getExtras().get("CONSUMER");
        tweets = new ArrayList<Tweet>();

        tweetArrayAdapter = new TweetAdapter(this, layoutId, tweets); // TODO;
        listView.setAdapter(tweetArrayAdapter);

        retrieveAndroidDevTweets();
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

    private void retrieveAndroidDevTweets() {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev&count=50";
        String signedUrl = signUrl(unsignedUrl);
        Log.i(TAG, "URL: " + signedUrl);
        new RetrieveTweetsByHashtagTask(this).execute(signedUrl);
    }

    void displayTweets(JSONObject response) {
        try {
            JSONArray statuses = response.getJSONArray(TAG_STATUSES);

            for (int i = 0; i < statuses.length(); i++) {
                JSONObject jsonObject = statuses.getJSONObject(i);
                JSONObject user = jsonObject.getJSONObject(TAG_USER);
                String screenName = user.getString(TAG_SCREEN_NAME);
                String location = user.getString(TAG_LOCATION);
                String text = jsonObject.getString(TAG_TEXT);

                Tweet tweet = new Tweet();
                tweet.setScreenName(screenName);
                tweet.setLocation(location);
                tweet.setText(text);

                tweets.add(tweet);
                tweetArrayAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String signUrl(String unsignedUrl) {
        if (unsignedUrl != null &&
                unsignedUrl != "") {
            try {
                return consumer.sign(unsignedUrl);
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
