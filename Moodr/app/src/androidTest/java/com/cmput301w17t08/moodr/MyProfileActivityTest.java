package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;
/**
 * Created by kskwong on 3/13/17.
 */

public class MyProfileActivityTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    Solo solo;

    MyProfileActivityTest(){
        super(com.cmput301w17t08.moodr.MyProfileActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void addMoodTest(){
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickOnButton(R.id.fab);

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);

    }

    public void seeMoodTest(){
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickOnButton(R.id.fab);

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);
        // add a Mood somehow.


    }
}
