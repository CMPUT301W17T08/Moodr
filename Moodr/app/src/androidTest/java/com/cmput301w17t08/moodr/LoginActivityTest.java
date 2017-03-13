package com.cmput301w17t08.moodr;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import junit.framework.TestCase;

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


    public void testSignUp(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        ElasticSearchUserController.IsExist isExist = new ElasticSearchUserController.IsExist();

        isExist.execute("bob");

        boolean exists = false;

        try{
            exists = (boolean) isExist.get();
        }
        catch(Exception e){
            Log.d("Error", "failed to get info from elastic search");
        }

        assertFalse(exists);

        solo.enterText((EditText) solo.getView(R.id.username), "bob");

        solo.clickOnButton("Login or Sign up");

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        isExist.execute("bob");

        try{
            exists = (boolean) isExist.get();
        }
        catch(Exception e){
            Log.d("Error", "failed to get info from elastic search");
        }

        assertTrue(exists);

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
    }

    public void testLogin(){
        // an existing user.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "bob");

        solo.clickOnButton("Login or Sign up");

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class); // does it go there or main?
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
