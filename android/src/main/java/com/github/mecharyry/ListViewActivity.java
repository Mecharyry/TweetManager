package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class ListViewActivity extends Activity {

    private static final String TAG = "ListViewActivity";
    private OAuthConsumer consumer;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        consumer = (OAuthConsumer) getIntent().getExtras().get("CONSUMER");
        editText = (EditText) findViewById(R.id.edit_text_tweets);

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

        new AsyncHttpClient().get(signUrl(unsignedUrl), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Call to retrieve timeline succeeded: JSONObject");
                editText.setText(response.toString(), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Call to retrieve timeline succeeded: JSONArray.");

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Object jsonObj = response.get(i);
                        sb.append(jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                editText.setText(sb, TextView.BufferType.EDITABLE);
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
