package com.cmput301w17t08.moodr;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * The mood class which holds all the relevant information for the mooditself
 *
 */
public class Mood implements Serializable{
    private Date date;
    private String owner;
    private int id;
    private Emotion emotion;
    private String imgUrl;
    private String trigger;
    private String situation;
    private String location;

    public Mood(String owner, Emotion emotion)
    {
        this.date = new Date(System.currentTimeMillis());
        this.emotion = emotion;
        this.owner = owner;
    }

    public String getUsername(){
        return owner;
    }
    public void setUsername(String Username){
        this.owner=Username;
    }


    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    public Emotion getEmotion(){
        return emotion;
    }
    public void setEmotion(Emotion emotion){
        this.emotion=emotion;
    }

    public int getId(){
        return id;
    }
    public void setId(int lastid){
        this.id=lastid+1;
    }

    public String getImgUrl(){
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTrigger(){
        return trigger;
    }
    public void setTrigger(String trigger) throws InvalidEntryException{
        if (trigger.length() > 20){
            throw new InvalidEntryException();
        }
        this.trigger=trigger;
    }

    public String getSituation(){
        return situation;
    }
    public void setSituation(String situation){
        this.situation=situation;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }
}
