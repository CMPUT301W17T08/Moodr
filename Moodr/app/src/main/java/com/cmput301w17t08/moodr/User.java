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
    private String user_ID;
    private ArrayList<Story> stories;

    public User(String username) {
        this.name = username;
        friends = new ArrayList<String>();
        pending = new ArrayList<String>();
        stories = new ArrayList<Story>();
        user_ID = "";
    }

    public User(){
        friends = new ArrayList<String>();
        pending = new ArrayList<String>();
        stories = new ArrayList<Story>();
        user_ID = "";
    }

    public void setUser_Id(String id) {
        this.user_ID = id;
    }

    public String getUser_Id() {
        return this.user_ID;
    }

    public void addPending(String pending){
        this.pending.add(pending);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public ArrayList<String> getPending() {
        return this.pending;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeFriend(String name){
        this.friends.remove(name);
    }

    public void removePending(String name){
        this.pending.remove(name);
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }

    public void addFriend(String name) {
        if (!this.friends.contains(name)) {
            this.friends.add(name);
        }
    }


    public void addStory(Story story){
        stories.add(story);
    }
    
    public Story getStory(int index){
        return stories.get(index);
    }

    public ArrayList<Story> getStories(){
        return stories;
    }
    public void setStories(ArrayList<Story> stories){
        this.stories.addAll(stories);
    }

    public void removeStory(int index){
        stories.remove(index);
    }
}
