package com.example.vikramjeet.codepathgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "733480bc31e24dde86138c383c2a0dab";
    private ArrayList<Photo> photoList;
    private PhotosAdapter adapter;
    private ListView lvPhotos;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // Create photoList (dataSource)
        photoList = new ArrayList<>();
        // Create the adapter and link it to the activity
        adapter = new PhotosAdapter(this, photoList);
        // Find ListView from layout
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Add adapter to the listView
        lvPhotos.setAdapter(adapter);
        // Send API request to fetch Photos
        fetchPopularPhotos();
        // Set up ListView Listener
        setUpListViewListener(lvPhotos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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

    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create the network client
        AsyncHttpClient httpClient = new AsyncHttpClient();

        // Make GET request
        httpClient.get(url, null, new JsonHttpResponseHandler() {
            //on success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("RESPONSE: ", response.toString());

                // clear old data from the photo list
                photoList.clear();

                //Iterate each of the photo json and create Photo object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");     //array of posts

                    int length = photosJSON.length();
                    // iterate array of posts
                    for (int index = 0; index < length; index++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(index);
                        // Create Photo Object
                        Photo photo = new Photo(photoJSON);
                        // Add photo to PhotoList
                        photoList.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Refresh adapter on data change
                adapter.notifyDataSetChanged();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            // on failure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("RESPONSE Error: ", responseString);

            }

        });
    }

    // ListView Listener methods

    private void setUpListViewListener(ListView lv) {
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        // Create intent to launch Comments Activity
                        Intent commentIntent = new Intent(PhotosActivity.this, CommentsActivity.class);
                        // Pass selected photo object to intent
                        commentIntent.putExtra("Photo", photoList.get(pos));
                        startActivity(commentIntent);
                    }
                }
        );
    }
}
