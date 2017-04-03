package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * This activity shows the mood of a friend. The mood is read only and does not allow editing or.
 * deleting.
 */
public class ViewFriendMoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mood);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        Intent intent = getIntent();
        Mood mood = (Mood) intent.getSerializableExtra("mood");

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        ft.add(R.id.mood_content, ViewMoodFragment.newInstance(mood));
        ft.commit();
    }
}
