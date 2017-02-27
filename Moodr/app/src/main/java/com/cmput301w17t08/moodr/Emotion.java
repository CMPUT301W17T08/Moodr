package com.cmput301w17t08.moodr;

/**
 * Created by kirsten on 2/27/17.
 */

public enum Emotion {
//    anger, confusion, disgust, fear, happiness, sadness, shame, and surprise.
    happy ("Yellow", ":)"),
    sad ("Blue", ":("),
    angry ("Red", "D:<"),
    confused("Purple", "???"),
    disgust("Green", "disgust"),
    fear("Maroon", "D:"),
    shame("Pink", ".////."),
    surprise ("yellow", "!!!");

    private final String color;
    private final String emoticon;

    Emotion(String color, String emoticon){
        this.color = color;
        this.emoticon = emoticon;
    }

    public String getColor() {
        return color;
    }

    public String getEmoticon() {
        return emoticon;
    }
}
