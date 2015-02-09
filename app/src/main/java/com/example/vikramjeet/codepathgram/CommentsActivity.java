package com.example.vikramjeet.codepathgram;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class CommentsActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "733480bc31e24dde86138c383c2a0dab";
    private ListView lvComments;
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Create DataSource
        comments = new ArrayList<Comment>();
        // Find ListView from data source
        lvComments = (ListView)findViewById(R.id.lvComments);

        // Retrieve photo object from Intent's extra property
        Photo photo = (Photo) this.getIntent().getSerializableExtra("Photo");

        // Fetch all the comments for given photoID
        fetchAllCommentForPhotoID(photo.getPhotoID());

        //Create Comment Adapter
        commentAdapter = new CommentAdapter(this, comments);
        // Link Adapter to the List View
        lvComments.setAdapter(commentAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchAllCommentForPhotoID(String photoID) {

        String url = String.format("https://api.instagram.com/v1/media/%s/comments?client_id=%s", photoID, CLIENT_ID);
        // Create the network client
        AsyncHttpClient httpClient = new AsyncHttpClient();

        // Make GET request
        httpClient.get(url, null, new JsonHttpResponseHandler() {
            //on success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("RESPONSE: ", response.toString());

                //Iterate each of the photo json and create Photo object
                JSONArray commentsJSON = null;
                try {
                    commentsJSON = response.getJSONArray("data");     //array of posts

                    int length = commentsJSON.length();
                    // iterate array of posts
                    for (int index = 0; index < length; index++) {
                        JSONObject commentJSON = commentsJSON.getJSONObject(index);
                        // Create Photo Object
                        Comment comment = new Comment(commentJSON);
                        // Add photo to PhotoList
                        comments.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Reverse the comments list to get the latest comment on top
                Collections.reverse(comments);
                // Refresh adapter on data change
                commentAdapter.notifyDataSetChanged();
            }

            // on failure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("RESPONSE Error: ", responseString);

            }

        });
    }
}
