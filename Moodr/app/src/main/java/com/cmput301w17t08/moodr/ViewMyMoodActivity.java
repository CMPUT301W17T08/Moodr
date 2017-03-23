package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity displays a selected mood of user who is logged into the app. In his activity, the
 * user can choose to edit or delete the mood. On delete, the user is returned to the list of their
 * moods.
 */
public class ViewMyMoodActivity extends ViewMoodActivity {
    Mood mood;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_mood);

        Intent intent = getIntent();

        index = intent.getIntExtra("index", -1);

        // if for whatever reason the mood index does not exist.
        try {
            mood = CurrentUserSingleton.getInstance().getMyMoodList().getMood(index);
        }
        catch (Exception e){
            Log.d("Error", "Invalid mood index");
        }

        loadMood(mood);

    }

    /**
     * deletes the current mood the user is viewing.
     */
    private void deleteMood(){
        CurrentUserSingleton.getInstance().getMyMoodList().delete(mood);

        //update on server
        ElasticSearchMoodController.DeleteMoodTask deleteMoodTask = new ElasticSearchMoodController.DeleteMoodTask();
        deleteMoodTask.execute(mood.getUsername(), Integer.toString(mood.getId()));

//        finish();
        Intent intent  = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    /**
     * starts EditMoodActivity to allow the user to edit the mood.
     */
    private void editMood(){
        Intent intent = new Intent(this, EditMoodActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mood_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_mood:
                editMood();
                return true;
            case R.id.delete_mood:
                deleteMood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
