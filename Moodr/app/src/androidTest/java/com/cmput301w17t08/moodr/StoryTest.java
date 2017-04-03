package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kirsten on 02/04/17.
 */

//02.07 Share Story
//    Combine a list of user's mood into a group as "story" and share with friend(s)
//    Story is sent and the receiver would get a notification

public class StoryTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    private Solo solo;

    public StoryTest() {
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

    public void testAddStory() {
        solo.clickOnView(solo.getView(R.id.storyButton));

        solo.enterText(0, "My Story");

        solo.clickOnText("Save");

        solo.clickOnCheckBox(1);
        solo.clickOnView(solo.getView(R.id.action_add_complete));

        solo.clickInList(0);

        solo.clickOnText("Send");
    }
}
