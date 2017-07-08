package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    Tweet infoTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        boolean option = getIntent().getExtras().getBoolean("option");
        // create the user fragment


        client = TwitterApp.getRestClient();
        if (option) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            // display the user timeline fragment inside the container (dynamically)

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            // make change
            ft.replace(R.id.flContainer, userTimelineFragment);
            // commit
            ft.commit();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    // deserialize the User object
                    try {
                        User user = User.fromJSON(response);
                        // set the title of the ActionBar based on the user information
                        getSupportActionBar().setTitle("@" + user.screenName);
                        // populate the user headline
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else{

            infoTweet = getIntent().getParcelableExtra("tweet");
            Long uid = infoTweet.uid;
            final String name = infoTweet.user.screenName;
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(name);
            // display the user timeline fragment inside the container (dynamically)

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            // make change
            ft.replace(R.id.flContainer, userTimelineFragment);
            // commit
            ft.commit();
            client.getUserProfile(name, Long.toString(uid), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // deserialize the User object

                    try {
                        User user = User.fromJSON(response);
                        // set the title of the ActionBar based on the user information
                        getSupportActionBar().setTitle("@" + name);
                        // populate the user headline
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;


    public void populateUserHeadline(User user){
        ButterKnife.bind(this);
       // TextView tvName = (TextView) findViewById(R.id.tvName);
       // TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
       // TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
       // TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

       // ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);

        tvTagline.setText(user.tagLine);
         tvFollowers.setText(user.followersCount + " Followers");
         tvFollowing.setText(user.followingCount + " Following");
        // load profile image with Glide
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }
}
