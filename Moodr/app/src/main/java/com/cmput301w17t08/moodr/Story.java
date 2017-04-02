package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * Created by kirsten on 01/04/17.
 */

public class Story {
    private String owner;
    private String name;
    private ArrayList<Mood> moodList;

    public Story(String owner, String name){
        this.owner = owner;
        this.name = name;
        moodList = new ArrayList<>();
    }

    public void addMoods(ArrayList<Mood> mood) {
        moodList.addAll(mood);
    }

    public ArrayList<Mood> getMoodList(){
        return moodList;
    }

    public String getOwner(){
        return owner;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return owner + " shared " + name + " story with you!";
    }
}
