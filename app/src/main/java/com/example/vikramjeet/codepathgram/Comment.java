package com.example.vikramjeet.codepathgram;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Vikramjeet on 2/8/15.
 */
public class Comment implements Serializable {

    private String commentorName;
    private String commentorPhotoURL;
    private String commentTxt;
    private String createdTime;

    public Comment(JSONObject commentJSON) {
        try {
            String timeString = commentJSON.getString("created_time");
            Long timeOfCreation = Long.parseLong(timeString);
            createdTime = DateUtils.getRelativeTimeSpanString(timeOfCreation * 1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();

            commentTxt = commentJSON.getString("text");
            commentorName = commentJSON.getJSONObject("from").getString("username");
            commentorPhotoURL = commentJSON.getJSONObject("from").getString("profile_picture");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCommentorName() {
        return commentorName;
    }

    public String getCommentorPhotoURL() {
        return commentorPhotoURL;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
