package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kskwong on 3/13/17.
 */

/*
Use cases
03.04 View follower list
03.06 View Stranger list
 */

public class FriendsActivityTest extends ActivityInstrumentationTestCase2<FriendsActivity> {
    private Solo solo;

    public FriendsActivityTest(){
        super(com.cmput301w17t08.moodr.FriendsActivity.class);
        // log in
        ElasticSearchUserController.GetUserTask getUser = new ElasticSearchUserController.GetUserTask();
        getUser.execute("bob");

        User user = new User();
        try {
            user = getUser.get().get(0);
        } catch (Exception e) {
            // nothing.
        }

        CurrentUserSingleton.getInstance().setSingleton(user);

        // populate all current user's mood
        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();
        ArrayList<Mood> moods = new ArrayList<>();
        getMoodTask.execute("bob");
        try {
            moods.addAll(getMoodTask.get());
            Collections.sort(moods, new Comparator<Mood>() {
                @Override
                public int compare(Mood mood, Mood t1) {
                    return t1.getDate().compareTo(mood.getDate());
                }
            });
        } catch (Exception e) {
            Log.d("Error", "Error getting moods from elastic search.");
        }
        CurrentUserSingleton.getInstance().getMyMoodList().setListOfMoods(moods);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
// Test will fail if request for user is pending
    public void testAddFriend() {
        solo.clickOnText("Search");
        solo.enterText(0, "x");

        solo.clickOnView(solo.getView(R.id.search_icon));

        solo.sleep(500);

        solo.enterText(0, "");

        solo.clickOnText("x");

        solo.waitForActivity(StrangerProfile.class);
        solo.assertCurrentActivity("Wrong Activity!", StrangerProfile.class);

        solo.clickOnView(solo.getView(R.id.stranger_button1));

        solo.waitForActivity(FriendsActivity.class);
        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);
    }



    public void testFriendProfile() {
        solo.clickInList(1,1);

        solo.waitForActivity(Profile.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
