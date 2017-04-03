package com.cmput301w17t08.moodr;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Canopy on 2017-04-02.
 * Saving Singleton here
 */

public class SaveSingleton {
    private  static final String FILENAME_USER = "UserSingleton.sav";
    private  static final String FILENAME_MOOD_LIST = "MoodListSingleton.sav";
    private  static final String FILENAME_OFFLINE_ACTIONS = "OfflineActionsSingleton.sav";

    Context context;

    public SaveSingleton(Context context) {
        this.context = context;
    }

    public void SaveSingletons() {
        saveUserSingletonInFile();
        saveMoodListSingletonInFile();
        saveOfflineActionsSingletonInFile();
    }

    public void LoadSingletons(){
        loadUserSingletonFromFile();
        loadMoodListSingletonFromFile();
        loadOfflineActionsSingletonFromFile();
    }

    public void LoadOfflineActionsSingleton(){
        loadOfflineActionsSingletonFromFile();
    }

    /**
     * Loads User Singleton from file.
     * @exception FileNotFoundException if the  file is not created.
     */
    private void loadUserSingletonFromFile() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME_USER);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            User user = gson.fromJson(in, new TypeToken<User>() {
            }.getType());
            fis.close();
            CurrentUserSingleton.getInstance().setSingleton(user);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error, unable to load singleton.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw  new RuntimeException();
        }
    }

    /**
     * Saves User Singleton in file in JSON format.
     * @throws FileNotFoundException if folder not exists.
     */
    private void saveUserSingletonInFile() {
        User user = CurrentUserSingleton.getInstance().getUser();
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_USER, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(user,out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Loads MoodList Singleton from file.
     * @exception FileNotFoundException if the  file is not created.
     */
    private void loadMoodListSingletonFromFile() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME_MOOD_LIST);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            MoodList moodList = gson.fromJson(in, new TypeToken<MoodList>() {
            }.getType());
            fis.close();
            CurrentUserSingleton.getInstance().setSingletonMyMoodList(moodList);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error, unable to load singleton.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw  new RuntimeException();
        }
    }

    /**
     * Saves MoodListSingleton in file in JSON format.
     * @throws FileNotFoundException if folder not exists.
     */
    private void saveMoodListSingletonInFile() {
        MoodList moodList = CurrentUserSingleton.getInstance().getMyMoodList();
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_MOOD_LIST, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(moodList,out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Loads OfflineActionsSingleton from file.
     * @exception FileNotFoundException if the  file is not created.
     */
    private void loadOfflineActionsSingletonFromFile() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME_OFFLINE_ACTIONS);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            OfflineMode offlineActions = gson.fromJson(in, new TypeToken<OfflineMode>() {
            }.getType());
            fis.close();
            CurrentUserSingleton.getInstance().setSingletonOfflineActions(offlineActions);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error, unable to load singleton.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw  new RuntimeException();
        }
    }

    /**
     * Saves OfflineActionsSingleton in file in JSON format.
     * @throws FileNotFoundException if folder not exists.
     */
    private void saveOfflineActionsSingletonInFile() {
        OfflineMode offlineActions = CurrentUserSingleton.getInstance().getMyOfflineActions();
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_OFFLINE_ACTIONS, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(offlineActions,out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
