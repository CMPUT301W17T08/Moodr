package com.cmput301w17t08.moodr;

/*
Parent Class for moods. Implements the main functionality for viewing individual moods.
 */

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class ViewMoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


//     Loads the mood information. Non-mandatory fields are checked if they exist first.

    protected void loadMood(Mood mood){
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_view_mood);
        TextView mood_name = (TextView) findViewById(R.id.viewMoodMood);
        ImageView mood_icon = (ImageView) findViewById(R.id.viewMoodIcon);
        TextView date = (TextView) findViewById(R.id.viewMoodDate);
        TextView social = (TextView) findViewById(R.id.viewMoodSocialSituation);
        TextView trigger = (TextView) findViewById(R.id.viewMoodTrigger);
        TextView location = (TextView) findViewById(R.id.viewMoodLocation);
        ImageView image = (ImageView) findViewById(R.id.viewMoodImage);

        setTitle(mood.getUsername());

        layout.setBackgroundColor(mood.getEmotion().getColor());

        mood_name.setText(mood.getEmotion().getName());

        // set emoticon
        mood_icon.setImageResource(mood.getEmotion().getEmoticon());

        // date needs to be converted to a string
        java.text.DateFormat dateFormat =  new SimpleDateFormat("MMM dd yyyy", Locale.US);

        date.setText(dateFormat.format(mood.getDate()));

        String situation = mood.getSituation();
        if (situation != null){
            social.setText(situation);
        }


        String trig = mood.getTrigger();
        if (trig != null){
            trigger.setText(trig);
        }


        Location loc = mood.getLocation();
        if (loc != null){
           location.setText(loc.toString());
        }


        String imgURL = mood.getImgUrl();
        if (imgURL != null){
            image.setImageURI(Uri.parse(mood.getImgUrl()));
        }
    }

}
