package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * Created by ZL on 3/4/2017.
 */
public class MoodList {
    private ArrayList<Mood> moodlist = new ArrayList<Mood>();

    public void add(Mood mood){
        if(moodlist.contains(mood)){
            throw new IllegalArgumentException();
        }
        else {
            moodlist.add(mood);
        }
    }

    public void delete(Mood mood){
        moodlist.remove(mood);
    }
}
