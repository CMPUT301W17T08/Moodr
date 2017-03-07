package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Sal on 2017-03-06.
 */

public class AddMood {
    public class AddRecord extends MoodPage {
        private ArrayList<Mood> moodlist;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mood);

            Intent intent = getIntent();


        }
    }
}