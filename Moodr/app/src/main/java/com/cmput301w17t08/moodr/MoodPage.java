package com.cmput301w17t08.moodr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Sal on 2017-03-06.
 */

public abstract class MoodPage extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
    }

    Spinner emotion = (Spinner) findViewById(R.id.sp_emotion);
    //String[] emotion_items = new String[];
    // EditText date = (EditText) findViewById(R.id.date);
    EditText socialSituation = (EditText) findViewById(R.id.et_social_situation);
    EditText trigger = (EditText) findViewById(R.id.et_trigger);
// EditText location = (EditText) findViewById(R.id.location);
// EditText image = (EditText) findViewById(R.id.image);
