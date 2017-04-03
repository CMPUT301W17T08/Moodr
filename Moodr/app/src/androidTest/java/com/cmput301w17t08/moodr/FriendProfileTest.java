package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.robotium.solo.Solo;

/*
06.02 View Friend Profile
 */

public class FriendProfileTest extends ActivityInstrumentationTestCase2<Profile> {
    private Solo solo;

    public FriendProfileTest() {
        super(com.cmput301w17t08.moodr.Profile.class);
        Intent intent = new Intent();
        intent.putExtra("name", "potato");
        setActivityIntent(intent);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testViewMood() {
        solo.assertCurrentActivity("Wrong Activity", Profile.class);

        solo.clickInList(1);

        solo.assertCurrentActivity("Wrong Activity", ViewFriendMoodActivity.class);
    }


    public void testFilter() {
        View view = solo.getView(R.id.filter_menu);
        solo.clickOnView(view);

        solo.clickOnText("Filter by Mood");
        solo.clickOnText("Happy");

        solo.waitForActivity(ViewFriendMoodActivity.class);

        ListView list = solo.getCurrentViews(ListView.class).get(0);
        assertEquals(2,list.getAdapter().getCount()); // the amount of happ moods already there is 2.
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
