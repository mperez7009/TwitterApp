package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
    private TextView profileName;
    private TextView userHandle;
    private TextView detailTweet;
    private TextView postedWhen;
    private ImageView profileImage;
    public Tweet details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        profileName = (TextView) findViewById(R.id.profileName);
        userHandle = (TextView) findViewById(R.id.userHandle);
        detailTweet = (TextView) findViewById(R.id.detailTweet);
        postedWhen = (TextView) findViewById(R.id.postedWhen);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        details = getIntent().getParcelableExtra("tweet");
        userHandle.setText("@" + details.user.screenName);
        profileName.setText(details.user.name);
        detailTweet.setText(details.body);
        postedWhen.setText(details.createdAt);
        Glide.with(this).load(details.user.profileImageUrl).bitmapTransform(new RoundedCornersTransformation(this, 3, 5)).into(profileImage);


    }
}
