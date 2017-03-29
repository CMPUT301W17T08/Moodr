package com.cmput301w17t08.moodr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        CurrentUserSingleton.getInstance().getMyMoodList().delete(mood);
        if (null == activeNetwork) {
            Toast.makeText(ViewMyMoodActivity.this, "You are offline.", Toast.LENGTH_SHORT).show();
            CurrentUserSingleton.getInstance().getMyOfflineActions().addAction(3, mood);
        }
        else {
            //update on server
            ElasticSearchMoodController.DeleteMoodTask deleteMoodTask = new ElasticSearchMoodController.DeleteMoodTask();
            deleteMoodTask.execute(mood);
        }
        finish();
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
                deleteOption().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog deleteOption() {
        return new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete Mood")
                .setMessage("Are you sure you want to delete this mood?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something
                        deleteMood();
                        Intent intent = new Intent(ViewMyMoodActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                        Toast.makeText(ViewMyMoodActivity.this, "Mood deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
