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

import junit.framework.TestCase;

/**
 * Created by kskwong on 3/13/17.
 */

public class FriendProfileTest extends ActivityInstrumentationTestCase2<Profile> {
    private Solo solo;

    public FriendProfileTest() {
        super(com.cmput301w17t08.moodr.Profile.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("name", "bob");
        setActivityIntent(intent);
        Activity activity = getActivity();
    }

    public void testViewMood() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", Profile.class);

        solo.clickInList(1);

        solo.assertCurrentActivity("Wrong Activity", ViewFriendMoodActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
