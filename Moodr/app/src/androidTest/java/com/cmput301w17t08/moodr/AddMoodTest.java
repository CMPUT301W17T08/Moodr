package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by kskwong on 3/13/17.
 */

public class AddMoodTest extends ActivityInstrumentationTestCase2<AddMoodActivity> {
    Solo solo;

    AddMoodTest() {
        super(com.cmput301w17t08.moodr.AddMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testAdd(){
        ArrayList<Mood> moods = new ArrayList<Mood>();

        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();

        getMoodTask.execute(CurrentUserSingleton.getInstance().getUser().getUsername());

        try{
            moods = (ArrayList<Mood>) getMoodTask.get();
        }
        catch (Exception e){
            Log.d("Error", "elastic search failed to get moods");
        }

        int length = moods.size();

        solo.assertCurrentActivity("Wrong Activity", AddMoodActivity.class);

        solo.pressSpinnerItem(0, 1);

        solo.clickOnActionBarItem(R.id.action_add_complete);

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

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