package com.cmput301w17t08.moodr;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This is an abstract activity for viewing details of individual moods.
 * Because the user can edit their own mood, but not others' moods, we created an abstract class
 * for the viewing moods in general.
 */
public abstract class ViewMoodActivity extends AppCompatActivity {

    public static Bitmap decodeImage(String imageString) {
        try {
            byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads the information contained in the mood. Anything that is blank is left blank.
     *
     * @param mood the mood to be loaded
     */
    protected void loadMood(Mood mood) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_view_mood);
        TextView mood_name = (TextView) findViewById(R.id.viewMoodMood);
        ImageView mood_icon = (ImageView) findViewById(R.id.viewMoodIcon);
        TextView date = (TextView) findViewById(R.id.viewMoodDate);
        TextView social = (TextView) findViewById(R.id.viewMoodSocialSituation);
        TextView trigger = (TextView) findViewById(R.id.viewMoodTrigger);
        TextView location = (TextView) findViewById(R.id.viewMoodLocation);
        ImageView image = (ImageView) findViewById(R.id.viewMoodImage);

        Bitmap imageToDisplay;

        // set title
        setTitle(mood.getUsername());

        // set background color
        layout.setBackgroundColor(mood.getEmotion().getColor());

        // set mood's name
        mood_name.setText(mood.getEmotion().getName());

        // set emoticon
        mood_icon.setImageResource(mood.getEmotion().getEmoticon());

        // set date
        // date needs to be converted to a string
        java.text.DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US);
        date.setText(dateFormat.format(mood.getDate()));

        // set situation
        String situation = mood.getSituation();
        if (situation != null) {
            social.setText(situation);
        }

        // set trigger
        String trig = mood.getTrigger();
        Log.d("Trigger", "Trigger is: " + mood.getTrigger());
        if (trig != null) {
            trigger.setText(trig);
        }

        // set location
        Coordinate loc = mood.getLocation();
        if (loc != null) {
            location.setText(loc.getLat().toString() + " " + loc.getLon().toString());
        }

        // set image
        String imgURL = mood.getImgUrl();
        if (imgURL != "") {
            imageToDisplay = decodeImage(imgURL);
            image.setImageBitmap(imageToDisplay);
        }
    }
}
