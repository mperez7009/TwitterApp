package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ReplyTweet extends AppCompatActivity {
    public TwitterClient client;
    public String tweet;
    final int REQUEST_CODE = 1;
    private EditText newTweet;
    private TextView counter;
    public Tweet replyTweet;
    Long uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient();
        counter = (TextView) findViewById(R.id.counter);
        newTweet = (EditText) findViewById(R.id.tweetComp);
        newTweet.addTextChangedListener(mTextEditorWatcher);
        replyTweet = getIntent().getParcelableExtra("tweet");
        uid = replyTweet.uid;
        newTweet.setText("@" + replyTweet.user.screenName);


    }



    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        //newTweet = (EditText) findViewById(R.id.tweetComp);
        //newTweet.addTextChangedListener(mTextEditorWatcher);
        tweet = newTweet.getText().toString();
        client.replyTweet(tweet, Long.toString(uid), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweetResponse = Tweet.fromJSON(response);
                    Intent data = new Intent(ReplyTweet.this, TimelineActivity.class);
                    data.putExtra("tweet", tweetResponse);
                    setResult(REQUEST_CODE, data);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            counter.setText(String.valueOf(140-s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


}
