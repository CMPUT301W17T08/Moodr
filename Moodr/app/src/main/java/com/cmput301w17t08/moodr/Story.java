package com.cmput301w17t08.moodr;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores the data required for a story.
 */

public class Story implements Serializable {
    private String owner;
    private String name;
    private ArrayList<Mood> moodList;

    public Story(String owner, String name) {
        this.owner = owner;
        this.name = name;
        moodList = new ArrayList<>();
    }

    public void addMoods(ArrayList<Mood> mood) {
        moodList.addAll(mood);
    }

    public ArrayList<Mood> getMoodList() {
        return moodList;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return owner + " shared " + name + " with you!";
    }
}
