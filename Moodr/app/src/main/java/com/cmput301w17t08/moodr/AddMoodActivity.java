package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class AddMoodActivity extends MoodPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        Intent intent = getIntent();

        }
}
