package com.cmput301w17t08.moodr;

import android.util.Log;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by kirsten on 23/03/17.
 */

public class MoodFilter{


    // Filter by elastic search. Only works if online.

    ArrayList<Mood> filterByEmotion(String name, String emotion){
        ArrayList<Mood> results = new ArrayList<Mood>();

        ElasticSearchMoodController.GetMoodEmotionTask getMoodEmotionTask =
                new ElasticSearchMoodController.GetMoodEmotionTask();

        getMoodEmotionTask.execute(name, emotion);

        try{
            results.addAll(getMoodEmotionTask.get());
        }
        catch(Exception e){
            Log.d("Error", "error getting results from elastic search");
        }

        return results;
    }

    ArrayList<Mood> filterMostRecent(String name){

        ArrayList<Mood> results = new ArrayList<Mood>();

        ElasticSearchMoodController.GetMostRecent getMostRecent =
                new ElasticSearchMoodController.GetMostRecent();

        getMostRecent.execute(name);

        try{
            results.addAll(getMostRecent.get());
        }
        catch(Exception e){
            Log.d("Error", "error getting results from elastic search");
        }


        return results;

    }

    ArrayList<Mood> filterKeyword (String name, String keyword){
        ArrayList<Mood> results = new ArrayList<Mood>();

        ElasticSearchMoodController.GetMoodKeyword getMoodKeyword =
                new ElasticSearchMoodController.GetMoodKeyword();

        getMoodKeyword.execute(name, keyword);

        try{
            results.addAll(getMoodKeyword.get());
        }
        catch(Exception e){
            Log.d("Error", "error getting results from elastic search");
        }


        return results;
    }
}
