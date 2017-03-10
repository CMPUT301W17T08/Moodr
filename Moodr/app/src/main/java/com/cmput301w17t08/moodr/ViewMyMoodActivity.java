package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// assumes that the index of the mood clicked is passed in.

public class ViewMyMoodActivity extends ViewMoodActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_mood);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);

        if (index < 0){
            // throw an error.
        }

        Mood mood = CurrentUserSingleton.getInstance().getMyMoodList().getMood(index);

        loadMood(mood);

    }

    private void deleteMood(Mood mood){
        CurrentUserSingleton.getInstance().getMyMoodList().delete(mood);
    }

    private void editMood(int index){
        // goes to edit mood page.

    }
}
