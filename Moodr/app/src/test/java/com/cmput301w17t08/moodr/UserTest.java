package com.cmput301w17t08.moodr;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by yunzhi on 2/27/17.
 */

public class UserTest {

    public void getUsername(){
        User user;
        user = new User();
        assertEquals(User.getUsername(), "name");
    }

    public void getEmail(){
        User user;
        user = new User();
        assertEquals(User.getEmail(), "email");

    }


    public void getMyFriendList(){
        User user;
        user = new User();
        ArrayList<String> testArray = new ArrayList<String>();
        assertEquals(null, User.getMyFrinedList());

        user.addFriend("friends");
        testArray.add("friends");
        assertEquals(user.getMyFrinedList(), testArray);

    }

    public void addFriend(){
        User user;
        user = new User();
        user.addFriend("friends");
        assertTrue(user.getMyFrinedList() != null);

    }


}
