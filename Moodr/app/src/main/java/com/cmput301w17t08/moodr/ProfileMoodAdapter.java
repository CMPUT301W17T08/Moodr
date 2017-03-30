package com.cmput301w17t08.moodr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class is a custom adapter for the profile pages. Includes filter to filter results. Filter
 * uses the posts stored in CurrentUserSingleton, so filters can be applied both online and offline.
 *
 * @see Profile
 * @see MyProfileActivity
 * <p>
 * Created by kirsten on 13/03/17.
 */


public class ProfileMoodAdapter extends ArrayAdapter<Mood> implements Filterable {
    private MoodFilterHolder moodFilter;
    private ArrayList<Mood> moods;

    public ProfileMoodAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
        this.moods = moods;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mood mood = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_mood_item, parent, false);
        }
        // Lookup view for data population
        TextView moodName = (TextView) convertView.findViewById(R.id.profileMood);
        TextView date = (TextView) convertView.findViewById(R.id.profilePostDate);
        ImageView icon = (ImageView) convertView.findViewById(R.id.profileMoodIcon);

        // Populate the data into the template view using the data object

        icon.setImageResource(mood.getEmotion().getEmoticon());

        moodName.setText(mood.getEmotion().getName());

        java.text.DateFormat format = new SimpleDateFormat("MMM-dd-yyyy hh:mm", Locale.US);
        date.setText(format.format(mood.getDate()));

        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public Filter getFilter() {
        if (moodFilter == null) {
            moodFilter = new MoodFilterHolder(this);
        }

        return moodFilter.getFilter();
    }

}

