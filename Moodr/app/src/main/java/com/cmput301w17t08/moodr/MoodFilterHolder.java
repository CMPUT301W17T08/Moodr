package com.cmput301w17t08.moodr;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kirsten on 30/03/17.
 */

public class MoodFilterHolder {
    ArrayAdapter<Mood> adapter;
    MoodFilter filter;
    ArrayList<Mood> allMoods = CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods();

    public MoodFilterHolder(ArrayAdapter adapter){
        this.adapter = adapter;
    }

    public MoodFilter getFilter(){
        if (filter == null){
            filter = new MoodFilter();
        }
        return filter;
    }

    private class  MoodFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null | constraint.length() == 0) {
                results.values = allMoods;
                results.count = allMoods.size();
            } else {
                ArrayList<Mood> resultsList = new ArrayList<Mood>();
                String filterBy = constraint.toString();

                if (filterBy.startsWith("E:")) { // filter by emotion
                    for (Mood mood : allMoods) {
                        if (mood.getEmotion().getName().equals(filterBy.substring(2))) {
                            resultsList.add(mood);
                        }
                    }
                } else if (filterBy.equals("LATEST")) { // filter latest
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.WEEK_OF_MONTH, -1);

                    for (Mood mood : allMoods) {
                        if (mood.getDate().after(now.getTime())) {
                            resultsList.add(mood);
                        }
                    }
                } else if (filterBy.startsWith("K:")) { // filter by keyword
                    for (Mood mood : allMoods) {
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
            if (results != null){
                adapter.clear();
                adapter.addAll((ArrayList<Mood>) results.values);
                adapter.notifyDataSetChanged();
            }
        }

    }
}
