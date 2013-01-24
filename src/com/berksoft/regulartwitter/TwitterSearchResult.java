package com.berksoft.regulartwitter;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TwitterSearchResult implements Parcelable {

    private String mFromUser;
    private String mText;
    
    public TwitterSearchResult(JSONObject twitterSearchResultJSON) {
        try {
            mFromUser = twitterSearchResultJSON.getString("from_user");
            mText = twitterSearchResultJSON.getString("text");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public TwitterSearchResult(Parcel in) {
        mFromUser = in.readString();
        mText = in.readString();
    }
    
    public String getFromUser() {
        return mFromUser;
    }
    
    public String getText() {
        return mText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFromUser);
        dest.writeString(mText);
    }
}
