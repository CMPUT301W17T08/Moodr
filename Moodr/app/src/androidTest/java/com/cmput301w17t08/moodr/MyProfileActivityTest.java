package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public void testAdd() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.fab));

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);

    }

    public void testViewMood(){
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.clickInList(1,1);

        solo.assertCurrentActivity("Wrong Activity", ViewMyMoodActivity.class);

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
