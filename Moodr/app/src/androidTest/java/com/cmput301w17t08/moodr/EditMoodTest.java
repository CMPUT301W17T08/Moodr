package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

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
        Intent intent = new Intent();
        intent.putExtra("index", 0);
        setActivityIntent(intent);

        Activity activity = getActivity();
    }

    // test loading
    // test editing emotion
    // test saving
    // test adding a situation


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}