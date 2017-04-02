package com.cmput301w17t08.moodr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity is a stranger's profile. This shows no moods and only allows the user to follow
 * the user if not already pending.
 *
 * If this user has requested to follow the logged in user, it will give the option to accept
 * or decline instead.
 * */
public class StrangerProfile extends AppCompatActivity {
    private Boolean status;
    private Button follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stranger_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        final String name = getIntent().getStringExtra("name");

        setTitle(name);

        follow = (Button) findViewById(R.id.follow);

        status = false;

        try {
            status  = checkPending(name);
        }
        catch(Exception e){
            Log.d("Error", "Error getting user pending list from Elastic Search");
        }

        if (status){
            follow.setText("Pending");
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!status){
                    try {
                        addPending(name);
                        follow.setText("Pending");
                    }
                    catch(Exception e){
                        Log.d("Error", "Error following user");
                    }

                }
            }
        });

    }

    /**
     * Checks if there is already a pending request for the user.
     * @param name of user pending
     * @return Boolean value. Whether if there is a pending request for the user.
     * @throws Exception
     */

    private Boolean checkPending(String name) throws Exception{ // change to more specific exception later.
        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            Toast.makeText(getApplicationContext(), "You are offline.", Toast.LENGTH_SHORT).show();
        } else {
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(name);
            try {
                User user2 = getUserTask.get().get(0); // get first user from result
                String currentUsername = CurrentUserSingleton.getInstance().getUser().getName();
                return user2.getPending().contains(currentUsername);
            } catch (Exception e) {
                Log.d("Error", "Unable to get user from elastic search");
                throw new Exception();
            }
        }

        return false;
    }

    /**
     * adds pending request for a given user.
     * @param name of user to send request to.
     * @throws Exception
     */

    private void addPending(String name) throws Exception {
        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            Toast.makeText(getApplicationContext(), "Unable to send request when offline.", Toast.LENGTH_SHORT).show();
        } else {
            User user2;

            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(name);

            try {
                user2 = getUserTask.get().get(0); // get first user from result
            } catch (Exception e) {
                Log.d("Error", "Unable to get user from elastic search");
                throw new Exception();
            }

            user2.addPending(CurrentUserSingleton.getInstance().getUser().getName());

            // update on elastic search
            ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
            updateUserTask.execute(user2);
        }
    }
}
