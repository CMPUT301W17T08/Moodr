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


    // username must be changed before running for this to pass.
    public void testSignUp(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        ElasticSearchUserController.GetUserTask getUserTask =
                new ElasticSearchUserController.GetUserTask();

        String username = "LoginIntentTest";
        getUserTask.execute(username);

        User user = null;

        try{
            user = getUserTask.get().get(0);
        }
        catch(Exception e){
            Log.d("Error", "failed to get info from elastic search");
        }

        assertNull(user);

        solo.enterText((EditText) solo.getView(R.id.username), username);

        solo.clickOnButton("SIGN UP");

        ElasticSearchUserController.GetUserTask getUserTask2  = new ElasticSearchUserController.GetUserTask();
        getUserTask2.execute(username);


        try{
            user = getUserTask2.get().get(0);
        }
        catch(Exception e){
            Log.d("Error", "failed to get info from elastic search");
        }

        assertEquals(user.getName(), username);

    }

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
