package com.cmput301w17t08.moodr;

/**
 * Created by kirsten on 2/27/17.
 */

public enum Emotion {
//    FIXME: fix all the colours and icon ids.
//    anger, confusion, disgust, fear, happiness, sadness, shame, and surprise.
    happy ("Yellow", ":)", "Happy"),
    sad ("Blue", ":(", "Sad"),
    angry ("Red", "D:<", "Angry"),
    confused("Purple", "???", "Confused"),
    disgust("Green", "disgust", "Disgust"),
    fear("Maroon", "D:", "Scared"),
    shame("Pink", ".////.", "Shame"),
    surprise ("yellow", "!!!", "Surprise");

    private final String color;
    private final String emoticon;
    private final String name;

    Emotion(String color, String emoticon, String name){
        this.color = color;
        this.emoticon = emoticon;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getEmoticon() {
        return emoticon;
    }

    public String getName() {return name; }
}
