package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This activity displays the user's moods. The user has the option to add a mood from this activity.
 * Due to the common load method, this extends Profile.
 * @see Profile
 */
public class MyProfileActivity extends Profile {
    private User user;
    private ArrayList<Mood> moods;
    private ProfileMoodAdapter adapter;
    private ListView moodsListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moods = CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods();
        user = CurrentUserSingleton.getInstance().getUser();
        setTitle(user.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMood();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        moodsListview = (ListView) findViewById(R.id.profile_moodlist);
        adapter = new ProfileMoodAdapter(this, moods);
        moodsListview.setAdapter(adapter);
        moodsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMood(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String name = CurrentUserSingleton.getInstance().getUser().getName();
        switch(item.getItemId()){
            case R.id.filter_recent:
                moods.clear();
                moods.addAll(new MoodFilter().filterMostRecent(name));
                break;
            case R.id.filter_angry:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.angry.getName().toLowerCase()));
                break;

            case R.id.filter_confused:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.confused.getName().toLowerCase()));
                break;

            case R.id.filter_disgusted:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.disgust.getName().toLowerCase()));
                break;

            case R.id.filter_happy:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.happy.getName().toLowerCase()));
                break;

            case R.id.filter_sad:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.sad.getName().toLowerCase()));
                break;

            case R.id.filter_scared:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.fear.getName().toLowerCase()));
                break;

            case R.id.filter_shame:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.shame.getName().toLowerCase()));
                break;

            case R.id.filter_surprise:
                moods.clear();
                moods.addAll(new MoodFilter().filterByEmotion(name, Emotion.surprise.getName().toLowerCase()));
                break;
        }

        adapter.notifyDataSetChanged();

        return true;
    }


    /**
     * goes to add mood activity to add a mood
     */
    private void addMood(){
        Intent intent  = new Intent(this, AddMoodActivity.class);
        startActivityForResult(intent,1);
    }

    /**
     * To view the details of the mood. Passes an index instead of a mood instance in the parent
     * @param i index of the mood in moodList
     */
    private void goToMood(int i){
        Intent intent = new Intent(this, ViewMyMoodActivity.class);
        intent.putExtra("index", i);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                for (Mood mood : moods){
                    Log.d("Mood", mood.getEmotion().getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
