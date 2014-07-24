package com.github.mecharyry.tweetlist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.mecharyry.AccessTokenPreferences;
import com.github.mecharyry.R;
import com.github.mecharyry.auth.oauth.OAuthAuthenticator;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class ListViewActivity extends Activity {

    private static final String TAG = "ListViewActivity";
    private TweetAdapter tweetArrayAdapter;
    private ListView listView;
    private OAuthConsumer consumer;
    private OAuthAuthenticator oAuthAuthentication;
    private AccessTokenPreferences accessTokenPreferences;
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oAuthAuthentication = OAuthAuthenticator.newInstance();
        accessTokenPreferences = AccessTokenPreferences.newInstance(this);
        consumer = oAuthAuthentication.getConsumer(accessTokenPreferences.retrieveAccessToken());
        requestManager = RequestManager.newInstance();

        setContentView(R.layout.activity_list_view);
        int layoutId = R.layout.tweets_list_item;
        listView = (ListView) findViewById(R.id.listview_tweets);

        tweetArrayAdapter = TweetAdapter.newInstance(this, layoutId);
        listView.setAdapter(tweetArrayAdapter);

        requestManager.requestAndroidDevTweets();
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
