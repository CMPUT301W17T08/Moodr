package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by kirsten on 29/03/17.
 */

public class FilterTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    private Solo solo;

    public FilterTest() {
        super(com.cmput301w17t08.moodr.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testFilterByEmotion() throws  Exception {
        solo.enterText((EditText) solo.getView(R.id.username), "potato");

        solo.clickOnButton("LOGIN");

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        assertTrue(solo.waitForActivity(MyProfileActivity.class.getSimpleName()));
        View view = solo.getView(R.id.filter_menu);
        solo.clickOnView(view);

        solo.clickOnText("Filter by Mood");
        solo.clickOnText("Happy");

        solo.sleep(1000);

        ListView list = solo.getCurrentViews(ListView.class).get(0);
        for (int i= 1 ; i <= list.getAdapter().getCount(); i++){
            solo.clickInList(i);
            solo.waitForText("Happy");
            solo.goBack();
        }
    }

    public void testFilterByKeyword() throws  Exception {
        solo.enterText((EditText) solo.getView(R.id.username), "potato");

        solo.clickOnButton("LOGIN");

        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        assertTrue(solo.waitForActivity(MyProfileActivity.class.getSimpleName()));
        View view = solo.getView(R.id.filter_keyword);
        solo.clickOnView(view);

        solo.enterText(0, "potato");


        solo.sleep(1000);

        ListView list = solo.getCurrentViews(ListView.class).get(0);
        for (int i= 1 ; i <= list.getAdapter().getCount(); i++){
            solo.clickInList(i);
            solo.waitForText("potato");
            solo.goBack();
        }
    }



    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}