package com.cmput301w17t08.moodr;

/**
 * Created by kskwong on 3/13/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.robotium.solo.Solo;

/**
 *
 *
 */

public class EditMoodTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    Solo solo;

    public EditMoodTest() {
        super(com.cmput301w17t08.moodr.MyProfileActivity.class);
        CurrentUserSingleton.getInstance().getUser().setName("bob");
        CurrentUserSingleton.getInstance().getMyMoodList().add(new Mood("bob", Emotion.sad));
        Intent intent = new Intent();
        intent.putExtra("index", 0);
        setActivityIntent(intent);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    // test loading
    public  void testLoad(){
       solo.clickOnText("Sad");
        solo.assertCurrentActivity("Wrong activity", ViewMyMoodActivity.class);

//        solo.clickOnActionBarItem(R.id.edit_mood);  does not click on this for some reason...
        solo.clickOnScreen(850 ,174);

        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);

    }
    // test editing emotion
    public void testEditEmotion(){
        solo.clickOnText("Sad");
        solo.assertCurrentActivity("Wrong activity", ViewMyMoodActivity.class);
        solo.clickOnScreen(850 ,174);
        solo.pressSpinnerItem(0,1);

        Spinner spinner = (Spinner) solo.getView(R.id.sp_emotion);

        String string  = spinner.getSelectedItem().toString();

        solo.clickOnScreen(850 ,174);

        solo.waitForText(string);
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}