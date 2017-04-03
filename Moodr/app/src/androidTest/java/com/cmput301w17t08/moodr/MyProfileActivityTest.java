package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
/**
 * Created by kskwong on 3/13/17.
 */

/*
Use Cases
06.01 View Current User Profile
 */

public class MyProfileActivityTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    Solo solo;

    public MyProfileActivityTest(){
        super(com.cmput301w17t08.moodr.MyProfileActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testAdd() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.fab));

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);

    }

    public void testViewMood(){
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickInList(1);

        solo.assertCurrentActivity("Wrong Activity", ViewMyMoodActivity.class);

    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
