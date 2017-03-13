package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * Created by ZL on 3/4/2017.
 */
public class MoodList {
    private ArrayList<Mood> listOfMoods;

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

    public Mood getMood(int index){
        return moodlist.get(index);
    }

    public void setListOfMoods (ArrayList<Mood> listOfMoods){
        this.listOfMoods = listOfMoods;
    }

    public ArrayList<Mood> getListOfMoods(){
        return this.listOfMoods;
    }
}
