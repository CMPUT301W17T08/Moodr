package com.cmput301w17t08.moodr;

/**
 * Created by kirsten on 3/9/17.
 */
public enum CurrentUserSingleton {
    INSTANCE;
    private final User user;
    private final MoodList myMoodList;

    // use setter methods to set user on log in or sign up or loading logged in user from file.
    CurrentUserSingleton() {
        user = new User("placeholder", "placeholder");
        myMoodList = new MoodList();
    }

    public static CurrentUserSingleton getInstance(){
        return INSTANCE;
    }

    public User getUser(){
        return user;
    }

    public MoodList getMyMoodList() {
        return myMoodList;
    }
}
