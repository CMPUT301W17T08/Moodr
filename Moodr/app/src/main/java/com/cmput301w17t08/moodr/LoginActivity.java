package com.cmput301w17t08.moodr;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


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
                if(validUser(UserName)){
                    Log.d("TEST", "THIS RUNS");
                    setCurrentUser(UserName);
                    Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button SignUpButton = (Button) findViewById(R.id.signup_button);
<<<<<<< HEAD
        loginText = (EditText) findViewById(R.id.username);
=======

>>>>>>> b12b9057782cfc63f78fafd18c50960b222c0eb2
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = loginText.getText().toString();
                if (!validUser(UserName)) {
                    createUser(UserName);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Username taken" , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
//    private boolean validUser (String name){
//        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
//        isExist.execute(name);
//        try {
//            if(isExist.get()){
//                return true;
//            } else{
//                Toast.makeText(getApplicationContext(),
//                        "Invalid User Name. Log in failed.",
//                        Toast.LENGTH_SHORT).show();
//                return false;}
//        } catch (Exception e) {
//            Log.i("Error", "Failed to get the User out of the async object");
//
//            return false;
//        }
//    }

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
            User user = new User(Username);
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
            CurrentUserSingleton.getInstance().getUser().setName(Username);
            startActivity(intent);
            return true;
        }catch (Exception e){
            Log.i("Error", "Failed to create the User");
            Toast.makeText(getApplicationContext(),
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
            Log.d("USERNAME", user.getName());
        }
        catch(Exception e){
            Log.d("ERROR", "Error getting user from elastic search");
        }

        User currentUser = CurrentUserSingleton.getInstance().getUser();
        currentUser.setName(user.getName());
        currentUser.setPostID(user.getPostID());
        currentUser.setFriends(user.getFriends());
        currentUser.setPending(user.getPending());
    }

}