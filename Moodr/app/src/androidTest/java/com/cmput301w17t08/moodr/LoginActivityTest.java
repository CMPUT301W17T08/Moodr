package com.cmput301w17t08.moodr;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;



/**
 * Created by kskwong on 3/13/17.
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public LoginActivityTest() {
        super(com.cmput301w17t08.moodr.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    // Use Case 01.01 Sign up
    // username must be changed before running for this to pass.
    public void testSignUp(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        ElasticSearchUserController.GetUserTask getUserTask =
                new ElasticSearchUserController.GetUserTask();


        String username = "LoginTest";

        solo.enterText(0, username);

        solo.clickOnButton("SIGN UP");

        solo.waitForText("New user created");

    }


    // use case 01.02 Log In
    public void testLogin(){
        // an existing user.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "bob");

        solo.clickOnButton("LOGIN");

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class); // does it go there or main?

        assertEquals(CurrentUserSingleton.getInstance().getUser().getName(), "bob");
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
