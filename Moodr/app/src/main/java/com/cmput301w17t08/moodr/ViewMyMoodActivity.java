package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// assumes that the index of the mood clicked is passed in.

public class ViewMyMoodActivity extends ViewMoodActivity {
    Mood mood;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_mood);

        Intent intent = getIntent();

        mood = CurrentUserSingleton.getInstance().getMyMoodList()
                .getMood(intent.getIntExtra("index", -1));


        // if for some reason the index is -1
        try {
            loadMood(mood);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    private void deleteMood(){
        CurrentUserSingleton.getInstance().getMyMoodList().delete(mood);
    }

    private void editMood(){
        // goes to edit mood page.

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
