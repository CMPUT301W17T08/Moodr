package com.cmput301w17t08.moodr;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by yunzhi on 2/27/17.
 */

public class UserTest {
    @Test
    public void getUsername(){
        User user;
        user = new User("john","john@ualberta.ca");
        assertEquals(user.getName(), "john");
    }


    @Test
    public void getMyFriendList(){
        User user;
        user = new User("test3","test3@email.email");
        ArrayList<String> testArray = new ArrayList<String>();
        assertEquals(null, user.getMyFriendList());

        user.addFriend("friends");
        testArray.add("friends");
        assertEquals(user.getMyFriendList(), testArray);

    }

    @Test
    public void addFriend(){
        User user;
        user = new User("something","email1");
        user.addFriend("friends");
        assertTrue(user.getMyFriendList() != null);

    }


}
