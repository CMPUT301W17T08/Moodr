package com.cmput301w17t08.moodr;

import junit.framework.Assert;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertTrue;

/**
 * Created by kirsten on 23/03/17.
 */

public class FilterUnitTest {
    //    ArrayList<Mood> filterByEmotion(String name, String emotion){
//        return null;
//    }
//    ArrayList<Mood> filterMostRecent(String name){
//        return null;
//    }
//    ArrayList<Mood> filterKeyword (String name, String keyword){
//        return null;
//    }
    @Test
    public void testFilterByEmotion() {
        // create and add moods to elastic search
        Mood mood1 = new Mood("filterEmotionTest", Emotion.surprise);
        mood1.setId(0);
        Mood mood2 = new Mood("filterEmotionTest", Emotion.sad);
        mood2.setId(1);
        Mood mood3 = new Mood("filterEmotionTest", Emotion.happy);
        mood3.setId(2);
        Mood mood4 = new Mood("filterEmotionTest", Emotion.surprise);
        mood4.setId(3);


        new ElasticSearchMoodController.AddMoodTask().execute(mood1);
        new ElasticSearchMoodController.AddMoodTask().execute(mood2);
        new ElasticSearchMoodController.AddMoodTask().execute(mood3);
        new ElasticSearchMoodController.AddMoodTask().execute(mood4);

        // filter the surprise mood from filterEmotionTest
        Filter filter = new Filter();
        ArrayList retrieved = filter.filterByEmotion("filterEmotionTest", "surprise");
        assertTrue(retrieved.contains(mood1));
        assertTrue(retrieved.contains(mood4));

        // delete added moods.
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "0");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "1");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "2");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "3");
    }

    @Test
    public void testFilterLatest() {
        // create and add moods to elastic search
        Mood mood1 = new Mood("filterEmotionTest", Emotion.surprise);
        mood1.setId(0);
        mood1.setDate(new GregorianCalendar(2017, 1, 31).getTime());
        Mood mood2 = new Mood("filterEmotionTest", Emotion.sad);
        mood2.setId(1);
        mood2.setDate(new GregorianCalendar(2017, 3, 23).getTime());
        Mood mood3 = new Mood("filterEmotionTest", Emotion.happy);
        mood3.setId(2);
        mood3.setDate(new GregorianCalendar(2017,3,1).getTime());
        Mood mood4 = new Mood("filterEmotionTest", Emotion.surprise);
        mood4.setId(3);
        mood4.setDate(new GregorianCalendar(2017,2,1).getTime());

        new ElasticSearchMoodController.AddMoodTask().execute(mood1);
        new ElasticSearchMoodController.AddMoodTask().execute(mood2);
        new ElasticSearchMoodController.AddMoodTask().execute(mood3);
        new ElasticSearchMoodController.AddMoodTask().execute(mood4);


        // get the latest moods
        Filter filter = new Filter();

        ArrayList<Mood> retrieved = filter.filterMostRecent("filterEmotionTest");
        assertTrue(retrieved.contains(mood2));

        // delete added moods.
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "0");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "1");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "2");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "3");
    }
    @Test
    public void testFilterKeyword(){
        // create and add moods.
        Mood mood1 = new Mood("filterEmotionTest", Emotion.surprise);
        mood1.setId(0);
        mood1.setSituation("test text here");

        Mood mood2 = new Mood("filterEmotionTest", Emotion.sad);
        mood2.setId(1);
        mood2.setSituation("some text");

        Mood mood3 = new Mood("filterEmotionTest", Emotion.happy);
        mood3.setId(2);
        mood3.setSituation("blah blah blah");

        Mood mood4 = new Mood("filterEmotionTest", Emotion.surprise);
        mood4.setId(3);
        mood4.setSituation("more text here");

        new ElasticSearchMoodController.AddMoodTask().execute(mood1);
        new ElasticSearchMoodController.AddMoodTask().execute(mood2);
        new ElasticSearchMoodController.AddMoodTask().execute(mood3);
        new ElasticSearchMoodController.AddMoodTask().execute(mood4);

        Filter filter = new Filter();
        ArrayList<Mood> retrieved =  filter.filterKeyword("filterEmotionTest","text");
        assertTrue(retrieved.contains(mood1));
        assertTrue(retrieved.contains(mood2));
        assertTrue(retrieved.contains(mood3));


        // delete added moods.
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "0");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "1");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "2");
        new ElasticSearchMoodController.DeleteMoodTask().execute("filterEmotionTest", "3");
    }
}
