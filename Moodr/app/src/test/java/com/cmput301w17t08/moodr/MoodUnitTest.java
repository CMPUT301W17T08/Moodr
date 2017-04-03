package com.cmput301w17t08.moodr;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * This class only contains getters and setters. None of them do anything special.
 */

public class MoodUnitTest {
    @Test
    public void testSetTriggerChar(){
        Mood mood = new Mood("guy", Emotion.angry);
            mood.setTrigger("some text here");
    }


}