package com.cmput301w17t08.moodr;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by kirsten on 02/04/17.
 */

public class StoryTest {
    @Test
    public void testToString(){
        Story story = new Story("Bob", "Bob's Story");
        assertEquals(story.toString(), "Bob shared Bob's Story with you!");
    }
}
