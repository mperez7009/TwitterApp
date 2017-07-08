package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
   // private TextView profileName;
   // private TextView userHandle;
   // private TextView detailTweet;
   // private TextView postedWhen;
   // private ImageView profileImage;
   // private ImageButton replyDet;
   // private ImageButton rtDet;
   // private ImageButton favDet;
    public Tweet details;
    public Tweet reTweets;
    public TwitterClient client;

    @BindView(R.id.profileName) TextView profileName;
    @BindView(R.id.userHandle) TextView userHandle;
    @BindView(R.id.detailTweet) TextView detailTweet;
    @BindView(R.id.postedWhen) TextView postedWhen;
    @BindView(R.id.profileImage) ImageView profileImage;
    @BindView(R.id.replyDet) ImageButton replyDet;
    @BindView(R.id.rtDet) ImageButton rtDet;
    @BindView(R.id.favDet) ImageButton favDet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

     //   profileName = (TextView) findViewById(R.id.profileName);
     //   userHandle = (TextView) findViewById(R.id.userHandle);
     //   detailTweet = (TextView) findViewById(R.id.detailTweet);
     //   postedWhen = (TextView) findViewById(R.id.postedWhen);
     //   profileImage = (ImageView) findViewById(R.id.profileImage);
     //   replyDet = (ImageButton) findViewById(R.id.replyDet);
        details = getIntent().getParcelableExtra("tweet");
     //   rtDet = (ImageButton) findViewById(R.id.rtDet);
     //   favDet = (ImageButton) findViewById(R.id.favDet);
        userHandle.setText("@" + details.user.screenName);
        profileName.setText(details.user.name);
        detailTweet.setText(details.body);
        postedWhen.setText(details.createdAt);
        Glide.with(this).load(details.user.profileImageUrl).bitmapTransform(new RoundedCornersTransformation(this, 3, 5)).into(profileImage);
        client = TwitterApp.getRestClient();

        replyDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DetailActivity.this, ReplyTweet.class);
                i.putExtra("tweet", details);
                startActivity(i);
            }
        });

        rtDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTweets = details;
                Log.d("Hello", reTweets.retweet.toString());
                if (reTweets.retweet) {
                    rtDet.setImageResource(R.drawable.ic_retweet);
                    reTweets.retweet = false;

                    client.unreTweet(reTweets, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                    });

                } else {
                    rtDet.setImageResource(R.drawable.ic_retweeted);
                    reTweets.retweet = true;
                    client.reTweet(reTweets, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }


                    });
                }
            }
        });

        favDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTweets = details;
                if (reTweets.favorite) {
                    favDet.setImageResource(R.drawable.ic_fav);
                    reTweets.favorite = false;

                    client.unFavTweet(reTweets, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                    });

                } else {
                    favDet.setImageResource(R.drawable.ic_favorite);
                    reTweets.favorite = true;
                    client.favTweet(reTweets, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            //       Toast.makeText(context, "suceeded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            //         Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });






    }
}
