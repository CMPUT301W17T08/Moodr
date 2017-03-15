package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * User class, holds information relaevant to the current user.
 * TODO: Outdated, get rid of email and perhaps password,
 */
public class User {
    private String name;
    private ArrayList<String> friends;
    private ArrayList<String> pending;

    public User(String username) {
        this.name = username;
        friends = new ArrayList<String>();
        pending = new ArrayList<String>();

    }

    public User(){
    }

    public String getUsername() {
        return name;
    }

    public ArrayList<String> getMyFriendList() {
        return friends;
    }

    public void addFriend(String friend) {
        friends.add(friend);
    }

    public void addPending(String pending){
        this.pending.add(pending);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public ArrayList<String> getPending() {
        return pending;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeFriend(String name){
        friends.remove(name);
    }
}
