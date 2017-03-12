package com.cmput301w17t08.moodr;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ZL on 2/25/2017.
 */
public class Mood implements Serializable{
    private Date date;
    private String owner;
    private int id;
    private Emotion emotion;
    private String imgUrl;
    private String trigger;
    private String situation;
    private Location location;

    public Mood(String owner, Emotion emotion)
    {
        this.owner = owner;
        this.emotion = emotion;
        this.date = new Date(System.currentTimeMillis());
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
    public void setImgUrl(String imgUrl) throws InvalidEntryException{
        if(imgUrl.length()>254){
            throw new InvalidEntryException();
        }
        this.imgUrl = imgUrl;
    }

    public String getTrigger(){
        return trigger;
    }
    public void setTrigger(String trigger) throws InvalidEntryException {
        if ((trigger.length() > 20) || ((trigger.length() - trigger.replace(".", "").length())>=3)){
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

    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location=location;
    }
}
