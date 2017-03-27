package com.cmput301w17t08.moodr;

/**
 *
 * This class is a singleton used to store an instance of the User class for the current logged in
 * user, and their mood list. The instance of this class is used throughout the app when information
 * of the user is needed.
 *
 * This singleton is implemented using a enum, guaranteeing that there will only ever be one
 * instance.
 */
public enum CurrentUserSingleton {
    /**
     * Instance current user singleton.
     */
    INSTANCE;
    private final User user;
    private final MoodList myMoodList;

    CurrentUserSingleton() {
        user = new User("placeholder");
        myMoodList = new MoodList();
    }

    /**
     * Get instance current user singleton.
     *
     * @return the current user singleton
     */
    public static CurrentUserSingleton getInstance(){
        return INSTANCE;
    }

    /**
     * Get user user.
     *
     * @return the user
     */
    public User getUser(){
        return user;
    }

    /**
     * Gets my mood list.
     *
     * @return the my mood list
     */

    public MoodList getMyMoodList() {
        return myMoodList;
    }

    /**
     * Reset singleton.
     */
    public void reset () {
        user.reset();
        myMoodList.reset();
    }
}
