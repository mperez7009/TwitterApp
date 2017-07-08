package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.tvUserName;

/**
 * Created by melissaperez on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    public SimpleDateFormat sf;
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);

        //populate the views according to the model
        holder.tvUsername.setText(tweet.user.name + " @" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).bitmapTransform(new RoundedCornersTransformation(context, 3, 5)).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create viewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
       // public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTime;
        public ImageButton reply;
    //    public ImageButton replyDet;
        public ImageButton retweet;
        public RelativeLayout relativeName;
        public ImageButton fav;
        TwitterClient client;
        Tweet reTweets;

        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewByID lookups
            client = TwitterApp.getRestClient();
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            reply = (ImageButton) itemView.findViewById(R.id.reply);
            relativeName = (RelativeLayout) itemView.findViewById(R.id.relativeName);
            retweet = (ImageButton) itemView.findViewById(R.id.retweet);
            fav = (ImageButton) itemView.findViewById(R.id.fav);



            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ReplyTweet.class);
                    i.putExtra("tweet", mTweets.get(position));
                    context.startActivity(i);
                }
            });


       /*     reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ReplyTweet.class);
                    i.putExtra("tweet", mTweets.get(position));
                    context.startActivity(i);
                }
            }); */

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("tweet", mTweets.get(position));
                    i.putExtra("option", false);
                    context.startActivity(i);
                }
            });

            relativeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("tweet", mTweets.get(position));
                    context.startActivity(i);

                }
            });
            retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    reTweets = mTweets.get(position);
                    Log.d("Hello", reTweets.retweet.toString());
                    if (reTweets.retweet) {
                        retweet.setImageResource(R.drawable.ic_retweet);
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

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(context, "suceeded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        retweet.setImageResource(R.drawable.ic_retweeted);
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

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(context, "suceeded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    reTweets = mTweets.get(position);
                    if (reTweets.favorite) {
                        fav.setImageResource(R.drawable.ic_fav);
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

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(context, "suceeded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        fav.setImageResource(R.drawable.ic_favorite);
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




            //retweet.setOnClickListener(new View.OnClickListener() {
              //  @Override
                //public void onClick(View v) {
                  //  int position = getAdapterPosition();
                    //Toast.makeText(context, "suceeded", Toast.LENGTH_SHORT).show();
                           // Intent i = new Intent(context, TimelineActivity.class);
                            //i.putExtra("tweet", mTweets.get(position));
                          //context.startActivity(i);


              //  }
                //  });
            //});
        }
    }








    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}


