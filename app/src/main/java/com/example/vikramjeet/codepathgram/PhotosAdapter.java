package com.example.vikramjeet.codepathgram;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikramjeet on 2/7/15.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {

    // View lookup cache
    private static class ViewHolder {
        ImageView ivUserPhoto;
        TextView tvUsername;
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvTimestamp;
        TextView tvLikesCount;
        TextView tvFirstComment;
        TextView tvSecondComment;
    }

    // Context is the activity
    public PhotosAdapter(Context context, List<Photo> photoList) {
        super(context, android.R.layout.simple_list_item_1, photoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the photo item for this position
        Photo photo = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        // Check if recycled view is present
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // convert new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            // Lookup the views
            viewHolder.ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
            viewHolder.tvFirstComment = (TextView) convertView.findViewById(R.id.tvFirstComment);
            viewHolder.tvSecondComment = (TextView) convertView.findViewById(R.id.tvSecondComment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Clear out the user photo imageview (in case previous image is still in there)
        viewHolder.ivUserPhoto.setImageResource(0);

        // Rounded image transformation
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(30)
                .oval(false)
                .build();


        // Insert the image using Picasso (asynchronous)
        Picasso.with(getContext())
                .load(photo.getUserPhotoURL())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivUserPhoto);

        // Populate photo data into each of the view items(cells)
        viewHolder.tvUsername.setText(photo.getUserName());

        // Create html string for different color for username and caption
        String htmlString = "<font size=\"12\" color=\"#181f8a\">%1$s </font> %2$s";
        String text = String.format(htmlString, photo.getUserName(), photo.getCaption());
        // Set Caption from Html string
        viewHolder.tvCaption.setText(Html.fromHtml(text));

        // Clear out the imageview (in case previous image is still in there)
        viewHolder.ivPhoto.setImageResource(0);
        // Insert the image using Picasso (asynchronous)
        Picasso.with(getContext())
                .load(photo.getImageURL())
                .placeholder(R.drawable.photo_placeholder)
                .error(R.drawable.photo_error)
                .into(viewHolder.ivPhoto);

        // Insert timestamp information
        viewHolder.tvTimestamp.setText(photo.getCreatedTime());

        // Insert likes count
        viewHolder.tvLikesCount.setText(Integer.toString(photo.getLikesCount()) + " likes");

        ArrayList comments = photo.getCommentList();

        if (comments != null) {
            // Insert first comment
            if (!comments.isEmpty()) {
                Comment comment = photo.getCommentList().get(0);
                String htmlCommentString = "<font size=\"12\" color=\"#181f8a\">%1$s </font> %2$s";
                String commentHtml = String.format(htmlCommentString, comment.getCommentorName(), comment.getCommentTxt());
                viewHolder.tvFirstComment.setText(Html.fromHtml(commentHtml));
            }
            // Insert second comment
            if (comments.size() > 1) {
                Comment comment = photo.getCommentList().get(1);
                String htmlCommentString = "<font size=\"12\" color=\"#181f8a\">%1$s </font> %2$s";
                String commentHtml = String.format(htmlCommentString, comment.getCommentorName(), comment.getCommentTxt());
                viewHolder.tvSecondComment.setText(Html.fromHtml(commentHtml));
            }
        }

        // Return the view(cell)
        return convertView;
    }
}
