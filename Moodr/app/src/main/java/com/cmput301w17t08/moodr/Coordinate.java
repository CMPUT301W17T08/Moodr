package com.cmput301w17t08.moodr;

import java.io.Serializable;

/**
 * Created by Canopy on 2017-03-31.
 */

public class Coordinate implements Serializable{
    private Double lat;
    private Double lon;

    public Coordinate(Double lat, Double lon) {

        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
