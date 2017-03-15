package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;


import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by kskwong on 3/13/17.
 *
 * class doesn't work.
 */

public class EditMoodTest extends ActivityInstrumentationTestCase2<EditMoodActivity> {
    Solo solo;

    EditMoodTest() {
        super(com.cmput301w17t08.moodr.EditMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        CurrentUserSingleton.getInstance().getMyMoodList().add(new Mood("bob", Emotion.sad));
        Intent intent = new Intent();
        intent.putExtra("index", 0);
        setActivityIntent(intent);

        Activity activity = getActivity();
    }

    // test loading
    public  void testLoad(){
        solo.searchText("sad");

    }
    // test editing emotion
    public void testEditEmotion(){
        solo.pressSpinnerItem(0,1); // no idea what this is, actually.

    }
    // test saving

    public void testSave(){

    }

    // test adding a field.
    public void testAddField(){

    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}