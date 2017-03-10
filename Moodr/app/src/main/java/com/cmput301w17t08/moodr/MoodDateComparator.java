package com.cmput301w17t08.moodr;

import java.util.Comparator;

/**
 * Created by kirsten on 3/9/17.
 *
 * Used to compare moods by date. (used when sorting by date)
 */


public class MoodDateComparator implements Comparator<Mood> {

    public int compare(Mood m1, Mood m2) {
        return m1.getDate().compareTo(m2.getDate());
    }
}