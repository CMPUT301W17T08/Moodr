//package com.cmput301w17t08.moodr;
//
//import org.junit.Test;
//
//import static junit.framework.TestCase.fail;
//
///**
// *
// *
// *
// */
//
//public class MoodUnitTest {
//    @Test
//    public void testSetTriggerChar(){
//        Mood mood = new Mood("guy", Emotion.angry);
//        try{
//            mood.setTrigger("thisisdefinitelyovertwentycharacters");
//            fail();
//        } catch (InvalidEntryException e) {
//
//        }
//
//
//    }
//
//    @Test
//    public void testSetTriggerWord(){
//        Mood mood = new Mood("guy", Emotion.angry);
//        try{
//            mood.setTrigger("This is more than three words");
//            fail();
//        } catch (InvalidEntryException e) {
//
//        }
//
//
//    }
//
//
//}
