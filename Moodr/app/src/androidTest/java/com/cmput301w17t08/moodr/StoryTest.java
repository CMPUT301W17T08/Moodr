package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

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
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
}
