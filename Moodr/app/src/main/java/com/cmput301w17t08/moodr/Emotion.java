package com.cmput301w17t08.moodr;

import android.graphics.Color;

/**
 * Created by kirsten on 2/27/17.
 */

public enum Emotion {
//    FIXME: fix all the colours and icon ids.
//    anger, confusion, disgust, fear, happiness, sadness, shame, and surprise.
    happy (Color.rgb(250, 255, 168), R.drawable.happy, "Happy"),
    sad (Color.rgb(204, 223, 255), R.drawable.sad, "Sad"),
    angry (Color.rgb(229, 103, 103), R.drawable.angry, "Angry"),
    confused(Color.rgb(177, 91, 183), R.drawable.confused, "Confused"),
    disgust(Color.rgb(172, 219, 129), R.drawable.disgust, "Disgust"),
    fear(Color.rgb(175, 77, 77), R.drawable.scared, "Scared"),
    shame(Color.rgb(255, 209, 209), R.drawable.embarassed, "Shame"),
    surprise (Color.rgb(255, 250, 109), R.drawable.surprised, "Surprise");

    private final int color;
    private final int emoticon;
    private final String name;

    Emotion(int color, int emoticon, String name){
        this.color = color;
        this.emoticon = emoticon;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public int getEmoticon() {
        return emoticon;
    }

    public String getName() {return name; }
}
