package com.cmput301w17t08.moodr;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * This class is the custom ArrayAdapter used to display the moods on the Latest Moods List.
 * Taken from https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 *
 */

public class LatestMoodListAdapter extends ArrayAdapter<Mood> {
    public LatestMoodListAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Mood mood = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.latest_mood_item, parent, false);
        }
        // Lookup view for data population
        TextView moodName = (TextView) convertView.findViewById(R.id.moodName);
        TextView friendName = (TextView) convertView.findViewById(R.id.friendName);
        TextView date = (TextView) convertView.findViewById(R.id.postDate);

        // Populate the data into the template view using the data object

        //moodName.setText(mood.getEmotion().getName());
        friendName.setText(mood.getUsername());

        java.text.DateFormat format = new SimpleDateFormat("MMM-dd-yyyy", Locale.US);
        date.setText(format.format(mood.getDate()));

        // Return the completed view to render on screen
        return convertView;
    }

}
