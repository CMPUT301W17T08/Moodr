package com.cmput301w17t08.moodr;

import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Created by zelin on 2/27/17.
 */

public class MoodUnitTest {
    @Test
    public void testSetTriggerChar(String trigger){
        Mood mood = new Mood("guy", Emotion.angry);
        try{
            mood.setTrigger("thisisdefinitelyovertwentycharacters");
            fail();
        } catch (InvalidEntryException e) {

        }


    }

    @Test
    public void testSetTriggerWord(String trigger){
        Mood mood = new Mood("guy", Emotion.angry);
        try{
            mood.setTrigger("This is more than three words");
            fail();
        } catch (InvalidEntryException e) {

        }


    }

    @Test
    public void testSetIMGURL(String imgurl){
        Mood mood = new Mood("guy", Emotion.angry);
        try{
            mood.setImgurl("WWW.thisisaveryaverylongimgurlbutitisstillverylonaverylongimgurlbsaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylongthisisaverylongimgurlbutitisstillverylong.com");
            fail();
        } catch (InvalidEntryException e) {

        }
    }


}
