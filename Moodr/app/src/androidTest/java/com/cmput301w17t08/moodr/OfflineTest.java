package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kirsten on 03/04/17.
 * - test add, edit, delete while offline
 */

public class OfflineTest extends ActivityInstrumentationTestCase2<MyProfileActivity>{
    private Solo solo;

    public OfflineTest(){
        super(com.cmput301w17t08.moodr.MyProfileActivity.class);

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
        solo.setWiFiData(false);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
        solo.waitForText("Unable to load moods from database");
    }

    public void testAdd(){
        solo.waitForText("Unable to load moods from database");
        ListView listview = (ListView) solo.getView(R.id.profile_moodlist);

        int count = listview.getChildCount();

        solo.clickOnView(solo.getView(R.id.fab));

        solo.waitForActivity(AddMoodActivity.class);

        solo.clickOnView(solo.getView(R.id.action_add_complete));

        solo.waitForActivity(MyProfileActivity.class);
        solo.sleep(1000);

        assertEquals(count+1, listview.getChildCount());

        solo.setWiFiData(true);
        solo.sleep(2000);

        solo.clickInList(1);
        solo.goBack();

        solo.waitForText("Synchronization completed.");

        solo.setWiFiData(false);
    }

    public void testEdit(){

    }

    public void testDelete(){

    }

    @Override
    public void tearDown() throws Exception{
        solo.setWiFiData(true);
        solo.finishOpenedActivities();
    }


}
