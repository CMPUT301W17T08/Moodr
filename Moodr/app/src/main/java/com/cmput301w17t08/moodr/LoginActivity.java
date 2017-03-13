package com.cmput301w17t08.moodr;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    CurrentUserSingleton.getInstance().getUser().setName(UserName);
                    startActivity(intent);
                }
            }
        });

    }
    private boolean validUser (String name){
        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();
        isExist.execute(name);
        try {
            if(isExist.get()){
                return true;
            } else{
                createUser(UserName);
                return false;}
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");

            return false;
        }
    }

    private boolean createUser(String Username){
        try {
            User user = new User(Username);
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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

}