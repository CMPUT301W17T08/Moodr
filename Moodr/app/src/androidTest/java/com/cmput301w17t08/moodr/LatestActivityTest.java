package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by kskwong on 3/13/17.
 */


public class LatestActivityTest extends ActivityInstrumentationTestCase2<LatestActivity> {
    private Solo solo;

    public LatestActivityTest() {
        super(com.cmput301w17t08.moodr.LatestActivity.class);
    }

    public void setUp() throws Exception {
        User user = CurrentUserSingleton.getInstance().getUser();
        user.setName("Potato");
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("aa");

        user.setFriends(friends);

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        CurrentUserSingleton.getInstance().getUser().setName("bob");
        Activity activity = getActivity();
    }

    public void testViewMood() {
        solo.assertCurrentActivity("Wrong Activity", LatestActivity.class);

        solo.clickInList(1);

        solo.assertCurrentActivity("Wrong Activity", ViewFriendMoodActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
