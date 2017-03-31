package com.cmput301w17t08.moodr;

import java.io.Serializable;
import java.util.Date;

/**
 * The mood class which holds all the relevant information for the mooditself
 */
public class Mood implements Serializable {
    private Date date;
    private String id;
    private String owner;
    private Emotion emotion;
    private String situation;
    private String trigger;
    private Coordinate location;
    private String imgUrl;

    public Mood(String owner, Emotion emotion) {
        this.date = new Date(System.currentTimeMillis());
        this.id = "";
        this.owner = owner;
        this.emotion = emotion;
        this.situation = "";
        this.trigger = "";
        this.imgUrl = "";
        this.location = new Coordinate();
    }

    public String getUsername() {
        return owner;
    }

    public void setUsername(String Username) {
        this.owner = Username;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        if (trigger.length() > 20) {
            ;
        }
        this.trigger = trigger;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }
}
