package com.cmput301w17t08.moodr;

import org.junit.Assert;
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
        user = new User("john");
        assertEquals(user.getName(), "john");
    }


    @Test
    public void getMyFriendList(){
        User user;
        user = new User("test3");
        ArrayList<String> testArray = new ArrayList<String>();
        assertEquals(null, user.getFriends());

        user.addFriend("friends");
        testArray.add("friends");
        assertEquals(user.getFriends(), testArray);

    }

    @Test
    public void testAddFriend(){
        User user = new User("test");
        Assert.assertEquals(user.getFriends().size(), 0);
        user.addFriend("Bob");

        Assert.assertTrue(user.getFriends().contains("Bob"));
        Assert.assertEquals(user.getFriends().size(), 1);

        user.addFriend("Bob");
        Assert.assertEquals(user.getFriends().size(), 0);
    }

    @Test
    public void testAddPending(){
        User user = new User("test");
        Assert.assertEquals(user.getPending().size(), 0);

        user.addPending("Bob");

        Assert.assertTrue(user.getPending().contains("Bob"));
        Assert.assertEquals(user.getPending().size(), 1);

        user.addFriend("Bob");
        Assert.assertEquals(user.getPending().size(), 0);
    }


}
