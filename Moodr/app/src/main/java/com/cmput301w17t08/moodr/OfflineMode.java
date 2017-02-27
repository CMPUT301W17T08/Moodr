package com.cmput301w17t08.moodr;

import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by salvindr on 2/27/17.
 */

public class OfflineMode {
    private ArrayList<Mood> addedMoodList;
    private ArrayList<Mood> editedMoodList;
    private ArrayList<Mood> deletedMoodList;


    public ArrayList getAddedMoodList(){
        return this.addedMoodList;
    }

    public ArrayList getEditMoodList(){
        return this.editedMoodList;
    }

    public ArrayList getDeleteMoodList(){
        return this.deletedMoodList;
    }

    public void addMoodWhileOffline(){

    }

    public void editMoodWhileOffline(){

    }

    public void deleteMoodWhileOffline(){

    }
}