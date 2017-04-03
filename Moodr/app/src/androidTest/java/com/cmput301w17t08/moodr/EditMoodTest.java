package com.cmput301w17t08.moodr;


import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 *
 */

public class EditMoodTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    Solo solo;

    public EditMoodTest() {
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

    // test loading
    public void testLoad() {
        solo.clickInList(1, 1);
        solo.assertCurrentActivity("Wrong activity", ViewMyMoodActivity.class);

//        solo.clickOnActionBarItem(R.id.edit_mood);  does not click on this for some reason...
        solo.clickOnView(solo.getView(R.id.edit_mood));

        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);

    }

    // test editing emotion
    public void testEditEmotion() {
        solo.clickInList(1, 1);
        solo.assertCurrentActivity("Wrong activity", ViewMyMoodActivity.class);

        solo.clickOnView(solo.getView(R.id.edit_mood
        ));
        solo.pressSpinnerItem(0, 1);

        Spinner spinner = (Spinner) solo.getView(R.id.sp_emotion);

        String string = spinner.getSelectedItem().toString();

        solo.clickOnView(solo.getView(R.id.action_edit_complete));

        solo.waitForActivity(ViewMyMoodActivity.class);
        solo.waitForText(string);
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

