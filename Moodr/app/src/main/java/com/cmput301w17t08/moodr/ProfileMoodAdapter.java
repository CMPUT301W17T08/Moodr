package com.cmput301w17t08.moodr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Boolean check = false;
    private ArrayList<Mood> checked;
    private Context context;

    public ProfileMoodAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
        this.moods = new ArrayList<Mood>();
        this.moods.addAll(moods);
        this.context = context;
    }

    public void setcheck(){
        if (check) {

            check = false;
            checked.clear();
        }
        else{
            check = true;
            checked = new ArrayList<Mood>();
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Mood mood = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_mood_item, parent, false);
        }


        if (check && (convertView.findViewById(R.id.moodCheckbox) == null) ){
            CheckBox checkbox = new CheckBox(context);
            checkbox.setId(R.id.moodCheckbox);

            LinearLayout item = (LinearLayout) convertView.findViewById(R.id.item);
            item.addView(checkbox, 0);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        checked.add(mood);
                    }
                    else{
                        checked.remove(mood);
                    }
                }

            });
        }
        else if (!check && convertView.findViewById(R.id.moodCheckbox) != null){
            LinearLayout item = (LinearLayout) convertView.findViewById(R.id.item);
            item.removeView(convertView.findViewById(R.id.moodCheckbox));
        }


        // Lookup view for data population
        TextView moodName = (TextView) convertView.findViewById(R.id.profileMood);
        TextView date = (TextView) convertView.findViewById(R.id.profilePostDate);
        ImageView icon = (ImageView) convertView.findViewById(R.id.profileMoodIcon);

        // Populate the data into the template view using the data object

        icon.setImageResource(mood.getEmotion().getEmoticon());

        moodName.setText(mood.getEmotion().getName());

        java.text.DateFormat format = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.US);
        date.setText(format.format(mood.getDate()));

        // Return the completed view to render on screen
        return convertView;
    }

    public void setMoods(ArrayList<Mood> moods){
        clear();
        addAll(moods);
        moodFilter.setMoods(moods);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (moodFilter == null) {
            moodFilter = new MoodFilterHolder(this, moods);
        }

        return moodFilter.getFilter();
    }

}

