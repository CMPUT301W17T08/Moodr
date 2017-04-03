package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by kirsten on 02/04/17.
 */

/* Use Cases
    02.05 Mood Detail
    02.02 Delete Mood
    02.03 Edit Mood
 */

public class ViewMyMoodTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    private Solo solo;

    public ViewMyMoodTest() {
        super(com.cmput301w17t08.moodr.MyProfileActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
}