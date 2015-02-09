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

import java.util.List;

/**
 * Created by Vikramjeet on 2/8/15.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    // View lookup cache
    private static class ViewHolder {
        ImageView ivCommentorPhoto;
        TextView tvCommentorName;
        TextView tvCommentTxt;
        TextView tvCommentTimestamp;
    }

    // Context is the activity
    public CommentAdapter(Context context, List<Comment> commentList) {
        super(context, android.R.layout.simple_list_item_1, commentList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the photo item for this position
        Comment comment = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        // Check if recycled view is present
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // convert new view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            // Lookup the views
            viewHolder.ivCommentorPhoto = (ImageView) convertView.findViewById(R.id.ivCommentorPhoto);
            viewHolder.tvCommentorName = (TextView) convertView.findViewById(R.id.tvCommentorName);
            viewHolder.tvCommentTxt = (TextView) convertView.findViewById(R.id.tvCommentTxt);
            viewHolder.tvCommentTimestamp = (TextView) convertView.findViewById(R.id.tvCommentTimestamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Clear out the user photo imageview (in case previous image is still in there)
        viewHolder.ivCommentorPhoto.setImageResource(0);

        // Rounded image transformation
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        // Insert the image using Picasso (asynchronous)
        Picasso.with(getContext())
                .load(comment.getCommentorPhotoURL())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivCommentorPhoto);

        // Populate photo data into each of the view items(cells)
        viewHolder.tvCommentorName.setText(comment.getCommentorName());

        // Create html string for different color for username and caption
        String htmlString = "<font size=\"12\" color=\"#181f8a\">%1$s </font> %2$s";
        String text = String.format(htmlString, comment.getCommentorName(), comment.getCommentTxt());
        // Set Caption from Html string
        viewHolder.tvCommentTxt.setText(Html.fromHtml(text));

        // Insert timestamp information
        viewHolder.tvCommentTimestamp.setText(comment.getCreatedTime());

        // Return the view(cell)
        return convertView;
    }
}
