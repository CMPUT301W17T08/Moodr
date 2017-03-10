package com.cmput301w17t08.moodr;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Canopy on 2017-03-09.
 */

public class Test {

    private ArrayList<Mood> moodList = new ArrayList<Mood>();
    private ArrayList<User> userList = new ArrayList<User>();

//  Get latest mood by usernames and store it to moodList in this case.
    public void LatestMoods(){
        ElasticSearchMoodController.GetLatestMoodsTask getLatestMoodsTask = new ElasticSearchMoodController.GetLatestMoodsTask();
        getLatestMoodsTask.execute("username1", "username2", "username3", "so on");

        try {
            moodList = getLatestMoodsTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }
    }

//    Add a new Mood to elastic search
    private void AddMood(Mood newMood) {
        ElasticSearchMoodController.AddMoodTask addMoodTask = new ElasticSearchMoodController.AddMoodTask();
        addMoodTask.execute(newMood);
    }

//    Get a list of mood by either just username or username and moodID. And store it to moodList in this case.
    private void GetMoods() {
        ElasticSearchMoodController.GetMoodTask getMoodTask = new  ElasticSearchMoodController.GetMoodTask();
        getMoodTask.execute("username", "moodID(optional)");

        try {
            moodList = getMoodTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }
    }

//    Delete moods by username and moodID
    private void DeleteMoods() {
        ElasticSearchMoodController.DeleteMoodTask deleteMoodTask = new ElasticSearchMoodController.DeleteMoodTask();
        deleteMoodTask.execute("username", "moodID");
    }

//    Add a new user to elastic search
    private void AddUser(User newUser) {
        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
        addUserTask.execute(newUser);
    }

//    Get a list of user by username and store it to userList in this case.
    private void GetUser() {
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute("username");

        try {
            userList = getUserTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Failed to get the users out of the async object");
        }
    }




}
