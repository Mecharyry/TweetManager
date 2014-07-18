package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
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

        Tweet tweet = new Tweet();
        tweet.setScreenName("screen01");
        tweet.setLocation("location01");
        tweet.setText("text01");
        tweets.add(tweet);

        tweetArrayAdapter = new TweetAdapter(this, layoutId, tweets); // TODO;
        listView.setAdapter(tweetArrayAdapter);

        retrieveAndroidDevTweets();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveAndroidDevTweets() {
        String unsignedUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23AndroidDev";
        String signedUrl = signUrl(unsignedUrl);
        Log.i(TAG, "URL: " + signedUrl);

        new AsyncHttpClient().get(signedUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Call to retrieve timeline succeeded: JSONObject");

                StringBuilder sb = new StringBuilder();

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
                        sb.append("Screen Name: " + screenName + " Location: " +
                                location + " Text: " + text + "\n\n");

                        tweetArrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Retrieved all tweets relating to Android Dev" + tweets);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Call to retrieve timeline succeeded: JSONArray.");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, "Retrieval Failed: JSONObject: " + errorResponse.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG, "On Finish.");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Log.i(TAG, "On Cancel.");
            }
        });
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
