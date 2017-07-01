package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by melissaperez on 6/26/17.
 */

public class Tweet implements Parcelable {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public SimpleDateFormat sf;
    public String date;
    public Boolean retweet;
    public Boolean favorite;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public SimpleDateFormat getSf() {
        return sf;
    }

    public String getDate() {
        return date;
    }

    public Boolean getRetweet() {
        return retweet;
    }

    public static Creator<Tweet> getCREATOR() {
        return CREATOR;
    }

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweet = jsonObject.getBoolean("retweeted");
        tweet.favorite = jsonObject.getBoolean("favorited");
        return tweet;


    }


    public Tweet() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
        dest.writeSerializable(this.sf);
        dest.writeString(this.date);
        dest.writeByte((byte)(retweet ? 1 : 0));
        dest.writeByte((byte)(favorite ? 1 : 0));
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.sf = (SimpleDateFormat) in.readSerializable();
        this.date = in.readString();
        this.retweet = in.readByte() != 0;
        this.favorite = in.readByte() != 0;
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}

