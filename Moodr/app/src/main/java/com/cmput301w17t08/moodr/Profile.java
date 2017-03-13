package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This activity displays the moods for a friend. The user can select a mood to view its details.
 */
public class Profile extends AppCompatActivity {
    private String name;
    private ArrayList<Mood> moods;
    private ProfileMoodAdapter adapter;
    private ListView moodsListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra("name");

        setTitle(name);

        moodsListview = (ListView) findViewById(R.id.profile_moodlist);

        final Button unfollow = (Button) findViewById(R.id.unfollow);
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unfollowUser(name);
                    finish();
                }
                catch(Exception e){
                }
        }});


        moodsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMood(moods.get(i));
            }
        });

    }

    /**
     * view details of mood.
     * @param mood to view
     *
     */

    private void goToMood(Mood mood){
        Intent intent = new Intent(this, ViewFriendMoodActivity.class);
        intent.putExtra("mood", mood);
        startActivity(intent);
    }


    /**
     * Loads the moods of a given user
     *
     * @param user the user
     * @return a list of moods
     */
    protected ArrayList<Mood> loadPosts(String user){
        ArrayList<Mood> moods = new ArrayList<>();

        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();

        getMoodTask.execute(user);

        try{
            moods.addAll(getMoodTask.get());
        }
        catch(Exception e){
            Log.d("Error", "Error getting moods from elastic search.");
        }

        return moods;
    }

    /**
     * Allows the user to unfollow this person
     * @param name of person to be unfollowed
     * @throws Exception
     */

    private void unfollowUser(String name) throws Exception{
        // remove from own list
        CurrentUserSingleton.getInstance().getUser().removeFriend(name);
        // update on ElasticSearch

        //remove from follow list of user 2
        User user2;

        ElasticSearchUserController.GetUserTask getUserTask
                = new ElasticSearchUserController.GetUserTask();

        getUserTask.execute(name);

        try{
            user2 = getUserTask.get().get(0);
        }
        catch(Exception e){
            throw new Exception();
        }


        user2.removeFriend(CurrentUserSingleton.getInstance().getUser().getName());
        // update on elastic search

    }

    @Override
    public void onStart() {
        super.onStart();
        moods = loadPosts(name);

        adapter = new ProfileMoodAdapter(this, moods);

        moodsListview.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
