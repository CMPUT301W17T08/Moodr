package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * Created by ZL on 2/25/2017.
 */
public class User {
    private String name;
    private String email;
    private MoodList moodlist;
    private ArrayList<String> friends;
    private ArrayList<String> pending;

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getMyFriendList() {
        return friends;
    }

    public void addFriend(String friend) {
        friends.add(friend);
    }
}
