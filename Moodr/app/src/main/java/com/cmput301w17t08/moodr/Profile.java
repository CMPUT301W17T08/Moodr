package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(Profile.this, MyProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_friends) {
            Intent intent = new Intent(Profile.this, FriendsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_nearme) {
            Intent intent = new Intent(Profile.this, MapsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_offline) {
            Intent intent = new Intent(Profile.this, OfflineMode.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            CurrentUserSingleton.getInstance().reset();
            Intent intent = new Intent(Profile.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_latest) {
            Intent intent = new Intent(this, LatestActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        // update on ElasticSearch - implement later

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
        // update on elastic search - implement later

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
