package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/*
Use Cases
03.01 Follow
03.02 Accept
03.03 Decline
06.03 View Stranger Profile
 */

public class StrangerProfileTest extends ActivityInstrumentationTestCase2<StrangerProfile> {
    private Solo solo;


    public StrangerProfileTest() {
        super(com.cmput301w17t08.moodr.StrangerProfile.class);

        Intent intent = new Intent();
        intent.putExtra("name", "Sally");
        setActivityIntent(intent);
    }

    public void setUp() throws Exception {
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
