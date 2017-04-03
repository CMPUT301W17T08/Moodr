package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Toolbar;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AddMoodTest extends ActivityInstrumentationTestCase2<AddMoodActivity> {
    Solo solo;

    public AddMoodTest() {
        super(com.cmput301w17t08.moodr.AddMoodActivity.class);
    }

    public void setUp() throws Exception {
        ElasticSearchUserController.GetUserTask getUser = new ElasticSearchUserController.GetUserTask();
        getUser.execute("bob");

        User user = new User();
        try {
            user = getUser.get().get(0);
        }
        catch(Exception e){
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
        MoodList userMoods = CurrentUserSingleton.getInstance().getMyMoodList();
        userMoods.setListOfMoods(moods);

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    // 02.01 Add Mood
    public void testAddMoodOnly(){
        ArrayList<Mood> moods = new ArrayList<Mood>();

        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();

        getMoodTask.execute(CurrentUserSingleton.getInstance().getUser().getName());

        try{
            moods = (ArrayList<Mood>) getMoodTask.get();
        }
        catch (Exception e){
            Log.d("Error", "elastic search failed to get moods");
        }

        int length = moods.size();

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);

        solo.pressSpinnerItem(0, 1);

        solo.clickOnView(solo.getView(R.id.action_add_complete));

        solo.waitForActivity(MyProfileActivity.class);

        try{
            moods = (ArrayList<Mood>) getMoodTask.get();
        }
        catch (Exception e){
            Log.d("Error", "elastic search failed to get moods");
        }

        assertNotSame(length, moods.size());
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}