package com.example.vikramjeet.codepathgram;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Vikramjeet on 2/7/15.
 */

public class Photo implements Serializable {

    private String type;
    private String userName;
    private String userPhotoURL;
    private String caption;
    private String imageURL;
    private String createdTime;
    private String photoID;

    public String getPhotoID() {
        return photoID;
    }

    private int imageHeight;
    private int likesCount;
//    private ArrayList<Spanned> comments;
    private ArrayList<Comment> commentList;

    public Photo(JSONObject json) {

        try {
            type = json.getString("type");
            userName = json.getJSONObject("user").getString("username");
            userPhotoURL = json.getJSONObject("user").getString("profile_picture");
            caption = json.getJSONObject("caption").getString("text");
            imageURL = json.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
            imageHeight = json.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
            likesCount = json.getJSONObject("likes").getInt("count");
            photoID = json.getString("id");

            JSONArray commentJSON = json.getJSONObject("comments").getJSONArray("data");
            commentList = getCommentListFromJSONArray(commentJSON);

            String timeString = json.getJSONObject("caption").getString("created_time");
            Long timeOfCreation = Long.parseLong(timeString);
            createdTime = DateUtils.getRelativeTimeSpanString(timeOfCreation * 1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhotoURL() {
        return userPhotoURL;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    private ArrayList<Comment> getCommentListFromJSONArray(JSONArray objects) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        try {
            int length = objects.length();
            // Iterate over all comment json objects
            for (int index = 0; index < length; index++) {
                Comment comment = new Comment(objects.getJSONObject(index));
                comments.add(comment);
                System.out.println("Comment date is : " + comment.getCreatedTime() + "and text is: " + comment.getCommentTxt());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.reverse(comments);

        return comments;
    }

}
