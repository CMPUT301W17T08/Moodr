package com.cmput301w17t08.moodr;

import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * When offline the app will place any new added/edited/deleted moods onto an array stored within
 * the device storage. When the device goes back online, this list is compared with the list
 * on elasticsearch and changes are made accordingly.
 *
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

    public void addMoodWhileOffline(Mood m){
        addedMoodList.add(m);
    }

    public void editMoodWhileOffline(Mood m){
        editedMoodList.add(m);

    }

    public void deleteMoodWhileOffline(Mood m){
        deletedMoodList.add(m);

    }
}