package com.cmput301w17t08.moodr;

import android.graphics.Color;

/**
 * This class stores information of all possible emotions in a Mood.
 * The separate class prevents clutter when getting information specific to the emotion portion
 * of the Mood.
 */
public enum Emotion {
    /**
     * Happy emotion.
     */
    happy(Color.rgb(250, 255, 168), R.drawable.happy, "Happy"),
    /**
     * Sad emotion.
     */
    sad(Color.rgb(204, 223, 255), R.drawable.sad, "Sad"),
    /**
     * Angry emotion.
     */
    angry(Color.rgb(229, 103, 103), R.drawable.angry, "Angry"),
    /**
     * Confused emotion.
     */
    confused(Color.rgb(177, 91, 183), R.drawable.confused, "Confused"),
    /**
     * Disgust emotion.
     */
    disgust(Color.rgb(172, 219, 129), R.drawable.disgust, "Disgust"),
    /**
     * Fear emotion.
     */
    fear(Color.rgb(175, 77, 77), R.drawable.scared, "Scared"),
    /**
     * Shame emotion.
     */
    shame(Color.rgb(255, 209, 209), R.drawable.embarassed, "Shame"),
    /**
     * Surprise emotion.
     */
    surprise(Color.rgb(255, 250, 109), R.drawable.surprised, "Surprise");

    private final int color;
    private final int emoticon;
    private final String name;

    Emotion(int color, int emoticon, String name) {
        this.color = color;
        this.emoticon = emoticon;
        this.name = name;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets emoticon.
     *
     * @return the emoticon
     */
    public int getEmoticon() {
        return emoticon;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
