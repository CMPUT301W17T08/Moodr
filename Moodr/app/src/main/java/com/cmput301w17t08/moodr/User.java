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
    private int post_ID;

    public User(String username) {
        this.name = username;
        friends = new ArrayList<String>();
        pending = new ArrayList<String>();
        post_ID = 0;

    }

    public User(){
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

    public void setPostID(int id){
        this.post_ID = id;
    }

    public int getPostID(){
        return this.post_ID;
    }

    public void incrementPostID(){
        this.post_ID += 1;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }
}
