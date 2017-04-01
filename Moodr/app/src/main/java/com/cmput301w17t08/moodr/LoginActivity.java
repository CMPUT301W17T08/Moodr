package com.cmput301w17t08.moodr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A login screen that offers login via user/password.
 * Current issue: Screen does not exit immediately, user must manually press the back button on
 * the phone.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    protected EditText loginText;
    private String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button SignInButton = (Button) findViewById(R.id.login_button);
        loginText = (EditText) findViewById(R.id.username);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = loginText.getText().toString();
                // Check if app is connected to a network.
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (null == activeNetwork) {
                    Toast.makeText(LoginActivity.this, "You are offline.", Toast.LENGTH_SHORT).show();
                } else {
                    if(validUser(UserName)){
                        setCurrentUser(UserName);
                        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button SignUpButton = (Button) findViewById(R.id.signup_button);

        loginText = (EditText) findViewById(R.id.username);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = loginText.getText().toString();
                // Check if app is connected to a network.
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (null == activeNetwork) {
                    Toast.makeText(LoginActivity.this, "You are offline.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validUser(UserName)) {
                        createUser(UserName);
                        Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validUser(String username){
        ArrayList<User> userList = new ArrayList<User>();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(username);

        try{
            userList = getUserTask.get();
        }
        catch(Exception e){
            Log.i("Error", "Error getting users out of async object");
        }

        if (userList.size() == 0){
            return false;
        }

        return true;
    }

    private boolean createUser(String Username){
        try {
            CurrentUserSingleton.getInstance().reset();
            User user = new User(Username);
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            try{
                String userId = addUserTask.get();
                CurrentUserSingleton.getInstance().getUser().setUser_Id(userId);
                Toast.makeText(LoginActivity.this, "New user created" , Toast.LENGTH_SHORT).show();

            }
            catch(Exception e){
                Log.i("Error", "Error getting user out of async object");
            }

            CurrentUserSingleton.getInstance().getUser().setName(Username);

            return true;
        }
        catch (Exception e){
            Log.i("Error", "Failed to create the User");
            Toast.makeText(LoginActivity.this,
                    "Can not create user. Internet connection Error",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setCurrentUser(String username){
        // populate all current user info here.
        ElasticSearchUserController.GetUserTask getUserTask
                = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(username);
        User user = new User();
        try{
            user = getUserTask.get().get(0);
        }
        catch(Exception e){
            Log.d("ERROR", "Error getting user from elastic search");
        }
        CurrentUserSingleton.getInstance().reset();
        User currentUser = CurrentUserSingleton.getInstance().getUser();
        currentUser.setName(user.getName());
        currentUser.setUser_Id(user.getUser_Id());
        currentUser.setFriends(user.getFriends());
        currentUser.setPending(user.getPending());

        // populate all current user's mood
        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();
        ArrayList<Mood> moods = new ArrayList<>();
        getMoodTask.execute(user.getName());
        try{
            moods.addAll(getMoodTask.get());
            Collections.sort(moods, new Comparator<Mood>() {
                @Override
                public int compare(Mood mood, Mood t1) {
                    return t1.getDate().compareTo(mood.getDate());
                }
            });
        }
        catch(Exception e){
            Log.d("Error", "Error getting moods from elastic search.");
        }
        MoodList userMoods = CurrentUserSingleton.getInstance().getMyMoodList();
        userMoods.setListOfMoods(moods);
    }
}