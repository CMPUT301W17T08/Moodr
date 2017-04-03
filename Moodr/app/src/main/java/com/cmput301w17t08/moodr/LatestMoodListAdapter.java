package com.cmput301w17t08.moodr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This class is the custom ArrayAdapter used to display the moods on the Latest Moods List.
 * This allows for the moods to be filtered, but unlike the ProfileMoodAdapter, this does not
 * allow the user to select multiple moods as a checklist.
 * <p>
 * Taken from https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class LatestMoodListAdapter extends ArrayAdapter<Mood> implements Filterable {
    MoodFilterHolder moodFilter;
    private ArrayList<Mood> origMoods;

    public LatestMoodListAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
        origMoods = new ArrayList<Mood>();
        origMoods.addAll(moods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mood mood = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.latest_mood_item, parent, false);
        }
        // Lookup view for data population
        TextView moodName = (TextView) convertView.findViewById(R.id.moodName);
        TextView friendName = (TextView) convertView.findViewById(R.id.friendName);
        TextView date = (TextView) convertView.findViewById(R.id.postDate);
        ImageView icon = (ImageView) convertView.findViewById(R.id.moodIcon);

        // Populate the data into the template view using the data object

        moodName.setText(mood.getEmotion().getName());
        icon.setImageResource(mood.getEmotion().getEmoticon());
        friendName.setText(mood.getUsername());

        java.text.DateFormat format = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.US);
        date.setText(format.format(mood.getDate()));

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (moodFilter == null) {
            moodFilter = new MoodFilterHolder(this, origMoods);
        }

        return moodFilter.getFilter();
    }

}
