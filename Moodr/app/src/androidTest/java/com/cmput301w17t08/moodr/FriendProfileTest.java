package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by kskwong on 3/13/17.
 */

public class FriendProfileTest extends ActivityInstrumentationTestCase2<Profile> {
    private Solo solo;
    String name = CurrentUserSingleton.getInstance().getUser().getFriends().get(0);

    public FriendProfileTest() {
        super(com.cmput301w17t08.moodr.Profile.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        setActivityIntent(intent);
        Activity activity = getActivity();
    }

    public void testViewMood() {
        solo.assertCurrentActivity("Wrong Activity", Profile.class);

        solo.clickInList(1);

        solo.assertCurrentActivity("Wrong Activity", ViewFriendMoodActivity.class);
    }

    public void testUnfollow(){
        solo.assertCurrentActivity("Wrong Activity", Profile.class);

        solo.clickOnButton("Unfollow");

        solo.assertCurrentActivity("Wrong Activity", FriendsActivity.class);

        assert(CurrentUserSingleton.getInstance().getUser().getFriends().contains(name));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
