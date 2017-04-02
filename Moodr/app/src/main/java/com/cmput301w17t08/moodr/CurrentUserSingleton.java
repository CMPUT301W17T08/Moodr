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
    private final OfflineMode myOfflineActions;

    CurrentUserSingleton() {
        user = new User("placeholder");
        myMoodList = new MoodList();
        myOfflineActions = new OfflineMode();
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
     * Gets my offline actions.
     *
     * @return OfflineMode object
     */
    public OfflineMode getMyOfflineActions() {
        return myOfflineActions;
    }


    /**
     * set current user on login.
     * @param user
     */
    public void setSingleton(User user){
        this.user.setName(user.getName());
        this.user.setUser_Id(user.getUser_Id());
        this.user.setPending(user.getPending());
        this.user.setFriends(user.getFriends());
        this.user.setStories(user.getStories());
    }

    /**
     * Reset singleton. used when logging out.
     */
    public void reset(){
        user.setName("");
        user.setFriends(null);
        user.setPending(null);
        user.setUser_Id(null);
        myMoodList.getListOfMoods().clear();
    }
}
