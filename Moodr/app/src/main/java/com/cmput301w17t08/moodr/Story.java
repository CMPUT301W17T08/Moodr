package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * Created by kirsten on 01/04/17.
 */

public class Story {
    private String owner;
    private String name;
    private ArrayList<Mood> moodList;

    public Story(String name, String owner){
        this.owner = owner;
        this.name = name;
    }

    public void addMood(Mood mood) {
        moodList.add(mood);
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
}
