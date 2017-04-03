package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by kskwong on 3/13/17.
 */

public class StrangerProfileTest extends ActivityInstrumentationTestCase2<StrangerProfile> {
    private Solo solo;


    public StrangerProfileTest() {
        super(com.cmput301w17t08.moodr.StrangerProfile.class);
    }

    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("name", "Sally");
        setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testLoad(){
        solo.searchText("Sally"); // should be the title
    }

    public void testFollow(){
        solo.clickOnButton("Add");
        solo.searchText("Pending");
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
