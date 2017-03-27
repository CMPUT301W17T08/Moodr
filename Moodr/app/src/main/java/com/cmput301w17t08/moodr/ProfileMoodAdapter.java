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
 * This class is a custom adapter for the profile pages.
 *
 * @see Profile
 * @see MyProfileActivity
 * <p>
 * Created by kirsten on 13/03/17.
 */


public class ProfileMoodAdapter extends ArrayAdapter<Mood> implements Filterable {
    private MoodFilter moodFilter;
    private ArrayList<Mood> moods;
    private ArrayList<Mood> origMoods = new ArrayList<>(); // keep a copy of the original moods.

    public ProfileMoodAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
        this.moods = moods;
        origMoods.addAll(moods);
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

    public void updateData(ArrayList moods){
        origMoods.clear();
        origMoods.addAll(moods);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (moodFilter == null) {
            moodFilter = new MoodFilter();
        }

        return moodFilter;
    }


    private class MoodFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null | constraint.length() == 0) {
                results.values = origMoods;
                results.count = origMoods.size();
            } else {
                ArrayList<Mood> resultsList = new ArrayList<Mood>();
                String filterBy = constraint.toString();

                if (filterBy.startsWith("E:")) { // filter by emotion
                    for (Mood mood : origMoods) {
                        if (mood.getEmotion().getName().equals(filterBy.substring(2))) {
                            resultsList.add(mood);
                        }
                    }
                } else if (filterBy.equals("LATEST")) { // filter latest
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.WEEK_OF_MONTH, -1);

                    for (Mood mood : origMoods) {
                        if (mood.getDate().after(now.getTime())) {
                            resultsList.add(mood);
                        }
                    }
                } else if (filterBy.startsWith("K:")) { // filter by keyword
                    for (Mood mood : origMoods) {
                        if (mood.getTrigger().contains(filterBy.substring(2))) {
                            resultsList.add(mood);
                        }
                    }
                }

                results.values = resultsList;
                results.count = resultsList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0){
                clear();
                moods.addAll((ArrayList<Mood>) results.values);
                notifyDataSetChanged();
            }
        }

    }

}

