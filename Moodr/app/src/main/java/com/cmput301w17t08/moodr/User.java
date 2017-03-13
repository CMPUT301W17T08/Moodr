package com.cmput301w17t08.moodr;

import java.util.ArrayList;

/**
 * User class, holds information relaevant to the current user.
 * TODO: Outdated, get rid of email and perhaps password,
 */
public class User {
    private String name;
    private String email;
    private ArrayList<String> friends;
    private ArrayList<String> pending;

    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.friends = new ArrayList<String>();
        this.pending = new ArrayList<String>();
    }

    public User() {

    }

    public User(String username) {

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void removeFriend(String name){
        friends.remove(name);
    }
}
